package org.example.delivery.service

import org.example.api.HomeAssignmentApiClient
import org.example.delivery.dto.*
import org.example.delivery.model.*
import org.example.venue.dto.*
import org.example.venue.model.*
import org.example.distance.model.*
import org.example.config.Constants.Api
import org.example.config.Constants.Delivery
import kotlin.math.*
import org.slf4j.LoggerFactory

class DeliveryOrderPriceCalculator(private val apiClient: HomeAssignmentApiClient) {
   private val logger = LoggerFactory.getLogger(javaClass)

   private suspend fun getStaticVenueInfo(venueSlug: String): StaticVenueInfo {
       val response: StaticVenueInfoOuterResponse = apiClient.get(Api.STATIC_VENUE_PATH.format(venueSlug))
       logger.debug("Static venue info response: $response")

       val lat = response.venue_raw.location.coordinates[1]
       val lon = response.venue_raw.location.coordinates[0]
       return StaticVenueInfo(lat, lon)
   }

   private suspend fun getDynamicVenueInfo(venueSlug: String): DynamicVenueInfo {
       val response: DynamicVenueInfoOuterResponse = apiClient.get(Api.DYNAMIC_VENUE_PATH.format(venueSlug))
       logger.debug("Dynamic venue info response: $response")

       val deliverySpecs = response.venue_raw.deliverySpecs
       val deliveryPricing = deliverySpecs.deliveryPricing

       val maxDeliveryDistance = deliveryPricing.distanceRanges
           .find { it.max == 0.0 }?.min
           ?: throw DeliveryNotPossibleException("Could not determine maximum delivery distance")

       return DynamicVenueInfo(
           maxDeliveryDistance = maxDeliveryDistance,
           orderMinimumNoSurcharge = deliverySpecs.orderMinimumNoSurcharge,
           basePrice = deliveryPricing.basePrice,
           distanceRanges = deliveryPricing.distanceRanges.map {
               DistanceRange(it.min, it.max, it.a, it.b)
           }
       )
   }

   suspend fun calculatePrice(request: DeliveryOrderRequest): DeliveryOrderResponse {
       validateCoordinates(request.userLat, request.userLon)

       val staticInfo = getStaticVenueInfo(request.venueSlug)
       val dynamicInfo = getDynamicVenueInfo(request.venueSlug)
       val distance = calculateDistance(request.userLat, request.userLon, staticInfo.lat, staticInfo.lon)
       logger.debug("Calculated distance: $distance meters")

       if (distance > dynamicInfo.maxDeliveryDistance) {
           throw DeliveryNotPossibleException("Delivery is not possible for this distance.")
       }

       val smallOrderSurcharge = calculateSmallOrderSurcharge(request.cartValue, dynamicInfo.orderMinimumNoSurcharge)
       val deliveryFee = calculateDeliveryFee(distance, dynamicInfo.basePrice, dynamicInfo.distanceRanges)
       val totalPrice = request.cartValue + smallOrderSurcharge + deliveryFee

       logger.debug("Small order surcharge: $smallOrderSurcharge")
       logger.debug("Delivery fee: $deliveryFee")
       logger.debug("Total price: $totalPrice")

       return DeliveryOrderResponse(
           totalPrice = totalPrice,
           smallOrderSurcharge = smallOrderSurcharge,
           cartValue = request.cartValue,
           delivery = DeliveryDetails(
               fee = deliveryFee,
               distance = distance.roundToInt()
           )
       )
   }

   private fun validateCoordinates(lat: Double, lon: Double) {
       require(lat in Delivery.MIN_LATITUDE..Delivery.MAX_LATITUDE) {
           "Latitude must be between ${Delivery.MIN_LATITUDE} and ${Delivery.MAX_LATITUDE}"
       }
       require(lon in Delivery.MIN_LONGITUDE..Delivery.MAX_LONGITUDE) {
           "Longitude must be between ${Delivery.MIN_LONGITUDE} and ${Delivery.MAX_LONGITUDE}"
       }
   }

   private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
       val dLat = Math.toRadians(lat2 - lat1)
       val dLon = Math.toRadians(lon2 - lon1)
       val a = sin(dLat / 2) * sin(dLat / 2) +
               cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
               sin(dLon / 2) * sin(dLon / 2)
       val c = 2 * atan2(sqrt(a), sqrt(1 - a))
       return Delivery.EARTH_RADIUS_METERS * c
   }

   private fun calculateSmallOrderSurcharge(cartValue: Int, orderMinimumNoSurcharge: Double): Int {
       return maxOf(0, (orderMinimumNoSurcharge - cartValue).toInt())
   }

   private fun calculateDeliveryFee(distance: Double, basePrice: Double, distanceRanges: List<DistanceRange>): Int {
       val range = distanceRanges
           .find { distance >= it.min && (it.max == 0.0 || distance < it.max) }
           ?: throw DeliveryNotPossibleException("Delivery is not possible for this distance.")

       return (basePrice + range.a + range.b * distance / 10).roundToInt()
   }
}
