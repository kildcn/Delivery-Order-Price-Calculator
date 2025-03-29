package org.example.venue.dto

import kotlinx.serialization.Serializable
import org.example.venue.model.VenueInfo

@Serializable
data class StaticVenueInfoOuterResponse(val venue: VenueInfo, val venue_raw: VenueRawInfo)
