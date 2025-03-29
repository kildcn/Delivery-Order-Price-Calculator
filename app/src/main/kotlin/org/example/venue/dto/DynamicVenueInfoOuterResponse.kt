package org.example.venue.dto

import kotlinx.serialization.Serializable

@Serializable
data class DynamicVenueInfoOuterResponse(val venue_raw: VenueRaw)
