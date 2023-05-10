package com.ldnhat.loraapp.common.model

import com.ldnhat.loraapp.enums.*

data class Data(
    val userId : String,
    val nodeId : String,
    val tokenKey : String,
    val temperature : String,
    val temperatureMeasure: TemperatureMeasure,
    val humidity : String,
    val humidityMeasure: HumidityMeasure,
    val lightIntensity : String,
    val lightIntensityMeasure: LightIntensityMeasure,
    val rssiIntensity : String,
    val rssiIntensityMeasure: RssiIntensityMeasure,
    val soilMoisture : String,
    val soilMoistureMeasure : SoilMoistureMeasure
)
