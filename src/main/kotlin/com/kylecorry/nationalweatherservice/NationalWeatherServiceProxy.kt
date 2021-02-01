package com.kylecorry.nationalweatherservice

import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import java.net.URL
import java.time.ZonedDateTime

class NationalWeatherServiceProxy {

    private val baseURL = "https://api.weather.gov"

    suspend fun getAlerts(latitude: Double, longitude: Double): List<Alert> {
        return coroutineScope {
            val alertUrl = "$baseURL/alerts/active?point=$latitude,$longitude"
            val json = URL(alertUrl).readText()
            val alertDto = Gson().fromJson(json, AlertDto::class.java)
            alertDto.features.filter { it.properties.status.toLowerCase() == "actual" }.map {
                Alert(
                    it.properties.event,
                    it.properties.parameters.NWSheadline.firstOrNull() ?: it.properties.headline,
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


    }

}