package org.example.delivery

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.delivery.service.DeliveryOrderPriceCalculator
import org.example.delivery.handlers.DeliveryOrderHandler

fun Application.configureDeliveryRouting(calculator: DeliveryOrderPriceCalculator) {
    routing {
        val deliveryHandler = DeliveryOrderHandler(calculator)

        get("/api/v1/delivery-order-price") {
            deliveryHandler.handleDeliveryOrderPrice(call)
        }
    }
}
