package org.example.delivery.handlers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.example.delivery.dto.DeliveryOrderRequest
import org.example.delivery.service.DeliveryOrderPriceCalculator
import org.example.delivery.service.DeliveryNotPossibleException
import org.example.error.ErrorCodes
import org.example.error.ErrorMessages
import org.example.error.ErrorResponse
import org.example.error.MissingParameterException

class DeliveryOrderHandler(private val calculator: DeliveryOrderPriceCalculator) {
    suspend fun handleDeliveryOrderPrice(call: ApplicationCall) {
        try {
            val request = validateAndCreateRequest(call)
            val response = calculator.calculatePrice(request)
            call.respond(response)
        } catch (e: MissingParameterException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = ErrorCodes.MISSING_REQUIRED_PARAMETER,
                    message = e.message ?: ErrorMessages.MISSING_VENUE_SLUG
                )
            )
        } catch (e: NumberFormatException) {
            val parameterName = when {
                e.message?.contains("cart_value") == true -> "cart_value"
                e.message?.contains("user_lat") == true -> "user_lat"
                e.message?.contains("user_lon") == true -> "user_lon"
                else -> null
            }

            val (code, message) = when (parameterName) {
                "cart_value" -> Pair(ErrorCodes.INVALID_CART_VALUE, ErrorMessages.INVALID_CART_VALUE)
                "user_lat" -> Pair(ErrorCodes.INVALID_USER_LAT, ErrorMessages.INVALID_USER_LAT)
                "user_lon" -> Pair(ErrorCodes.INVALID_USER_LON, ErrorMessages.INVALID_USER_LON)
                else -> Pair(ErrorCodes.INVALID_CART_VALUE, ErrorMessages.INVALID_CART_VALUE)
            }

            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(code = code, message = message)
            )
        } catch (e: DeliveryNotPossibleException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = ErrorCodes.DELIVERY_NOT_POSSIBLE,
                    message = ErrorMessages.DELIVERY_NOT_POSSIBLE
                )
            )
        } catch (e: IllegalArgumentException) {
            val message = e.message?.lowercase() ?: ""
            val (code, errorMessage) = when {
                message.contains("cart_value") ->
                    Pair(ErrorCodes.INVALID_CART_VALUE, ErrorMessages.INVALID_CART_VALUE)
                message.contains("user_lat") ->
                    Pair(ErrorCodes.INVALID_USER_LAT, ErrorMessages.INVALID_USER_LAT)
                message.contains("user_lon") ->
                    Pair(ErrorCodes.INVALID_USER_LON, ErrorMessages.INVALID_USER_LON)
                else ->
                    Pair(ErrorCodes.INVALID_CART_VALUE, ErrorMessages.INVALID_CART_VALUE)
            }

            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(code = code, message = errorMessage)
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = ErrorCodes.VENUE_NOT_FOUND,
                    message = ErrorMessages.VENUE_NOT_FOUND
                )
            )
        }
    }

    private fun validateAndCreateRequest(call: ApplicationCall): DeliveryOrderRequest {
        val venueSlug = call.parameters["venue_slug"]
            ?: throw MissingParameterException("venue_slug")

        val cartValue = call.parameters["cart_value"]?.toIntOrNull()
            ?: throw NumberFormatException("cart_value")

        val userLat = call.parameters["user_lat"]?.toDoubleOrNull()
            ?: throw NumberFormatException("user_lat")

        val userLon = call.parameters["user_lon"]?.toDoubleOrNull()
            ?: throw NumberFormatException("user_lon")

        return DeliveryOrderRequest(venueSlug, cartValue, userLat, userLon)
    }
}
