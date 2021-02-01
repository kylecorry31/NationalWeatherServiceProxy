package com.kylecorry.nationalweatherservice

import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import java.net.URL
import java.net.URLEncoder
import java.time.ZonedDateTime
import javax.net.ssl.HttpsURLConnection

class NationalWeatherServiceProxy(val apiKey: String) {

    private val baseURL = "https://api.weather.gov"

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getAlerts(latitude: Double, longitude: Double): List<Alert> {
        return coroutineScope {
            val alertUrl =
                "$baseURL/alerts/active?point=${
                    URLEncoder.encode(
                        "${latitude},${longitude}",
                        "utf-8"
                    )
                }"
            val connection = URL(alertUrl).openConnection() as HttpsURLConnection
            // For now they say to use the User-Agent with an identifier for your app - later to be replaced by a key
            connection.setRequestProperty("User-Agent", apiKey)
            connection.inputStream.use {
                val json = connection.inputStream.bufferedReader().readText()
                val alertDto = Gson().fromJson(json, AlertDto::class.java)
                alertDto.features.filter { it.properties.status.toLowerCase() == "actual" }.map {
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
        }


    }

}