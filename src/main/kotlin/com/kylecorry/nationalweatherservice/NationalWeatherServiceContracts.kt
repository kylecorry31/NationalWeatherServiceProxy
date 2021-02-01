package com.kylecorry.nationalweatherservice

internal class AlertDto {
    lateinit var features: List<AlertFeaturesDto>
}

internal class AlertFeaturesDto {
    lateinit var properties: AlertPropertiesDto
}

internal class AlertPropertiesDto {
    var id = ""
    var sent = ""
    var effective = ""
    var onset = ""
    var expires = ""
    var ends = ""
    var status = ""
    var messageType = ""
    var category = ""
    var severity = ""
    var certainty = ""
    var urgency = ""
    var event = ""
    var headline = ""
    var description = ""
    var instruction = ""
    var response = ""
    lateinit var parameters: AlertParametersDto
}

internal class AlertParametersDto {
    var NWSheadline = listOf<String>()
    var HazardType = listOf<String>()
}

internal class ZoneDto {
    lateinit var features: List<ZoneFeaturesDto>
}

internal class ZoneFeaturesDto {
    lateinit var properties: ZonePropertiesDto
}

internal class ZonePropertiesDto {
    var id = ""
    var name = ""
    var effectiveDate = ""
    var expirationDate = ""
    var state = ""
    lateinit var cwa: List<String>
    var radarStation = ""
}

internal class StationsDto {
    lateinit var features: List<StationDto>
}

internal class StationDto {
    lateinit var properties: StationPropertiesDto
}

internal class StationPropertiesDto {
    lateinit var stationIdentifier: String
}

internal class GridPointsDto {
    lateinit var properties: GridPointsPropertiesDto
}

internal class GridPointsPropertiesDto {
    lateinit var gridId: String
    lateinit var gridX: String
    lateinit var gridY: String
}

internal class ForecastDto {
    lateinit var properties: ForecastPropertiesDto
}

internal class ForecastPropertiesDto {
    lateinit var updated: String
    lateinit var periods: List<ForecastPeriodDto>
}

internal class ForecastPeriodDto {
    lateinit var name: String
    lateinit var detailedForecast: String
}