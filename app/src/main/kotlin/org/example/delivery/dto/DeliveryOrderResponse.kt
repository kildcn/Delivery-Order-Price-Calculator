package org.example.delivery.dto

import kotlinx.serialization.Serializable
import org.example.delivery.model.DeliveryDetails

@Serializable
data class DeliveryOrderResponse(
    val totalPrice: Int,
    val smallOrderSurcharge: Int,
    val cartValue: Int,
    val delivery: DeliveryDetails
)
