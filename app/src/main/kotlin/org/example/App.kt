package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.example.api.HomeAssignmentApiClient
import org.example.delivery.configureDeliveryRouting
import org.example.delivery.service.DeliveryOrderPriceCalculator

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }

    val apiClient = HomeAssignmentApiClient()
    val calculator = DeliveryOrderPriceCalculator(apiClient)

    configureDeliveryRouting(calculator)
}
