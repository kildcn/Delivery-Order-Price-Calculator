package org.example.delivery.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryOrderRequest(
    val venueSlug: String,
    val cartValue: Int,
    val userLat: Double,
    val userLon: Double
)
