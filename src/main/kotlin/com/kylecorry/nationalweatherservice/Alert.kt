package com.kylecorry.nationalweatherservice

import java.time.ZonedDateTime

data class Alert(
    val id: String,
    val event: String,
    val headline: String,
    val description: String,
    val instructions: String,
    val hazard: List<String>,
    val sent: ZonedDateTime,
    val effective: ZonedDateTime,
    val onset: ZonedDateTime,
    val expires: ZonedDateTime,
    val ends: ZonedDateTime,
    val category: String,
    val severity: String,
    val certainty: String,
    val urgency: String,
)