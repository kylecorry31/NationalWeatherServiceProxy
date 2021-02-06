package com.kylecorry.nationalweatherservice

import java.time.ZonedDateTime

data class Alert(
    val id: String,
    val event: String,
    val headline: String,
    val description: String,
    val instructions: String?,
    val hazard: List<String>,
    val sent: ZonedDateTime?,
    val effective: ZonedDateTime?,
    val onset: ZonedDateTime?,
    val expires: ZonedDateTime?,
    val ends: ZonedDateTime?,
    val category: String,
    val severity: AlertSeverity,
    val certainty: AlertCertainty,
    val urgency: AlertUrgency,
)

enum class AlertUrgency {
    Unknown,
    Past,
    Future,
    Expected,
    Immediate
}

enum class AlertSeverity {
    Unknown,
    Minor,
    Moderate,
    Severe,
    Extreme
}

enum class AlertCertainty {
    Unknown,
    Unlikely,
    Possible,
    Likely,
    Observed
}