package org.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.example.delivery.dto.DeliveryOrderResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppTest {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun testDeliveryOrderPrice() = testApplication {
        application {
            module()
        }

        val testParams = mapOf(
            "venue_slug" to "home-assignment-venue-berlin",
            "cart_value" to "1000",
            "user_lat" to "52.4903",
            "user_lon" to "13.4536"
        )

        val response = client.get("/api/v1/delivery-order-price?" + testParams.entries.joinToString("&") { "${it.key}=${it.value}" })
        assertEquals(HttpStatusCode.OK, response.status)
        val jsonResponse = json.decodeFromString<DeliveryOrderResponse>(response.bodyAsText())

        val formattedResponse = """
            |{
            |  "total_price": ${jsonResponse.totalPrice},
            |  "small_order_surcharge": ${jsonResponse.smallOrderSurcharge},
            |  "cart_value": ${jsonResponse.cartValue},
            |  "delivery": {
            |    "fee": ${jsonResponse.delivery.fee},
            |    "distance": ${jsonResponse.delivery.distance}
            |  }
            |}
            """.trimMargin()

        println(formattedResponse)

        // Basic assertions to ensure the response structure
        assertEquals(1000, jsonResponse.cartValue)
        assertTrue { jsonResponse.totalPrice > 0 }
        assertTrue { jsonResponse.delivery.fee >= 0 }
        assertTrue { jsonResponse.delivery.distance > 0 }
    }
}
