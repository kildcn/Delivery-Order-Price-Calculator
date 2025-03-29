package org.example.delivery.model

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryGeoRange(val bbox: String?, val type: String, val coordinates: List<List<List<Double>>>)
