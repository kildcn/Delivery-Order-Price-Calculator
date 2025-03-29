package org.example.delivery.model

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryDetails(
    val fee: Int,
    val distance: Int
)
