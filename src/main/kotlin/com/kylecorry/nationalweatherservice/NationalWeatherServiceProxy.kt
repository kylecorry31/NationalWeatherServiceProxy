package com.kylecorry.nationalweatherservice

import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import java.net.URL
import java.net.URLEncoder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.HttpsURLConnection

class NationalWeatherServiceProxy(val apiKey: String) {

    private val baseURL = "https://api.weather.gov"

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun get(url: String): String {
        return coroutineScope {
            val connection = URL(url).openConnection() as HttpsURLConnection
            // For now they say to use the User-Agent with an identifier for your app - later to be replaced by a key
            connection.setRequestProperty("User-Agent", apiKey)
            connection.inputStream.use {
                connection.inputStream.bufferedReader().readText()
            }
        }
    }

    suspend fun getObservations(latitude: Double, longitude: Double, since: ZonedDateTime): List<Observation> {
        val formattedTime = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:SSZ").format(since)
        val zone = getZone(latitude, longitude) ?: return listOf()
        val json = get("$baseURL/zones/forecast/${zone}/observations?start=${formattedTime}")
        val observationsDto = Gson().fromJson(json, ObservationsDto::class.java)
        return convertObservations(observationsDto)
    }

    suspend fun getForecast(latitude: Double, longitude: Double): List<Forecast> {
        val zone = getZone(latitude, longitude) ?: return listOf()
        val json = get("$baseURL/zones/forecast/${zone}/forecast")
        val forecastDto = Gson().fromJson(json, ForecastDto::class.java)
        return forecastDto.properties.periods.map {
            val shortForecastEnd = it.detailedForecast.indexOf(".")
            Forecast(it.name, it.detailedForecast.substring(0, shortForecastEnd), it.detailedForecast.substring(shortForecastEnd + 1).trim())
        }
    }

    suspend fun getLatestObservations(latitude: Double, longitude: Double): Observation? {
        val zone = getZone(latitude, longitude) ?: return null
        val json = get("$baseURL/zones/forecast/${zone}/observations?limit=1")
        val observationsDto = Gson().fromJson(json, ObservationsDto::class.java)
        return convertObservations(observationsDto).firstOrNull()
    }

    private suspend fun getZone(latitude: Double, longitude: Double): String? {
        val json = get(
            "$baseURL/zones/forecast?point=${
                URLEncoder.encode(
                    "${latitude},${longitude}",
                    "utf-8"
                )
            }"
        )

        val zoneDto = Gson().fromJson(json, ZoneDto::class.java)
        return zoneDto.features.firstOrNull()?.properties?.id
    }

    suspend fun getAlerts(latitude: Double, longitude: Double): List<Alert> {
        val json = get(
            "$baseURL/alerts/active?point=${
                URLEncoder.encode(
                    "${latitude},${longitude}",
                    "utf-8"
                )
            }"
        )

        val alertDto = Gson().fromJson(json, AlertDto::class.java)
        return alertDto.features.filter { it.properties.status.toLowerCase() == "actual" }.map {
            Alert(
                it.properties.id,
                it.properties.event,
                it.properties.parameters.NWSheadline.firstOrNull()
                    ?: it.properties.headline,
                it.properties.description.replace("\n\n", "\\newline").replace("\n", " ")
                    .replace("\\newline", "\n\n"),
                it.properties.instruction,
                it.properties.parameters.HazardType,
                ZonedDateTime.parse(it.properties.sent),
                ZonedDateTime.parse(it.properties.effective),
                ZonedDateTime.parse(it.properties.onset),
                ZonedDateTime.parse(it.properties.expires),
                ZonedDateTime.parse(it.properties.ends),
                it.properties.category,
                it.properties.severity,
                it.properties.certainty,
                it.properties.urgency
            )
        }


    }

    private fun convertObservations(dto: ObservationsDto): List<Observation> {
        // TODO: Do unit conversions to metric if needed
        return dto.features.map {
            val p = it.properties
            Observation(
                ZonedDateTime.parse(p.timestamp),
                weather = p.presentWeather.filter { weather -> weather.weather != null }.map { weather ->
                    WeatherObservation(weather.weather!!, weather.intensity)
                },
                elevation = p.elevation?.value,
                temperature = p.temperature?.value,
                dewpoint = p.dewpoint?.value,
                windDirection = p.windDirection?.value,
                windSpeed = p.windSpeed?.value,
                windGust = p.windGust?.value,
                barometricPressure = p.barometricPressure?.value,
                seaLevelPressure = p.seaLevelPressure?.value,
                visibility = p.visibility?.value,
                maxTemperatureLast24Hours = p.maxTemperatureLast24Hours?.value,
                minTemperatureLast24Hours = p.minTemperatureLast24Hours?.value,
                precipitationLast3Hours = p.precipitationLast3Hours?.value,
                relativeHumidity = p.relativeHumidity?.value,
                windChill = p.windChill?.value,
                heatIndex = p.heatIndex?.value,
                clouds = p.cloudLayers.filter { cloud -> cloud.amount != null }.map { cloud ->
                    CloudObservation(
                        amount = cloud.amount!!,
                        base = cloud.base?.value
                    )
                }
            )
        }
    }
}