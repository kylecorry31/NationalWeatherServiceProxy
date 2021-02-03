package com.kylecorry.nationalweatherservice

import java.time.ZonedDateTime

data class Observation(
    val time: ZonedDateTime,
    val weather: List<WeatherObservation>,
    val elevation: Float? = null,
    val temperature: Float? = null,
    val dewpoint: Float? = null,
    val windDirection: Float? = null,
    val windSpeed: Float? = null,
    val windGust: Float? = null,
    val barometricPressure: Float? = null,
    val seaLevelPressure: Float? = null,
    val visibility: Float? = null,
    val maxTemperatureLast24Hours: Float? = null,
    val minTemperatureLast24Hours: Float? = null,
    val precipitationLast3Hours: Float? = null,
    val relativeHumidity: Float? = null,
    val windChill: Float? = null,
    val heatIndex: Float? = null,
    val clouds: List<CloudObservation> = listOf()
)

data class CloudObservation(val amount: String, val base: Float? = null)

data class WeatherObservation(val name: String, val intensity: String?)