package com.celllocator.app.entities

data class CellDetails(
    val networkType: String = "?",
    val mcc: String? = "?",
    val mnc: String? = "?",
    val tac: String? = "?",
    val rsrp: Int,
    val signalStrength: Int,
    val cellNr: Int?,
    val sectorId: Int,
    val rfcn: Int,
    val pci: Int?,
    val rsrq: Int?,
    val sinr: Int?,
    val bandNr: Int,
    val bandName: String? = "?",
    val rxFrequency: Double,
    val txFrequency: Double
)
