package org.example.venue.model

import kotlinx.serialization.Serializable
import org.example.delivery.model.DeliveryGeoRange

@Serializable
data class VenueInfo(
    val id: String,
    val image_url: String,
    val delivery_geo_range: DeliveryGeoRange
)
