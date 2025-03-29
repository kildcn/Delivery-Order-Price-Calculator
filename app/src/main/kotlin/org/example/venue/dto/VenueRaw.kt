package org.example.venue.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.example.delivery.model.DeliverySpecs

@Serializable
data class VenueRaw(
    @SerialName("delivery_specs")
    val deliverySpecs: DeliverySpecs
)
