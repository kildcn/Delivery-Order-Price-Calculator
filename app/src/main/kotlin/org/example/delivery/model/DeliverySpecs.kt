package org.example.delivery.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeliverySpecs(
    @SerialName("delivery_enabled")
    val deliveryEnabled: Boolean,
    @SerialName("order_minimum_no_surcharge")
    val orderMinimumNoSurcharge: Double,
    @SerialName("delivery_pricing")
    val deliveryPricing: DeliveryPricing
)
