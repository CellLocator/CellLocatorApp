package com.celllocator.app.entities

import cz.mroczis.netmonster.core.model.connection.IConnection
import cz.mroczis.netmonster.core.model.connection.NoneConnection
import cz.mroczis.netmonster.core.model.connection.PrimaryConnection
import cz.mroczis.netmonster.core.model.connection.SecondaryConnection

data class Cell(
    var networkType: String = "?",
    var mcc: String? = "?",
    var mnc: String? = "?",
    var tac: String? = "?",
    var connectionType: IConnection,
    var rsrp: Int,
    var signalStrength: Int,
    var cellId: Long,
    var sectorId: Int,
    var rfcn: Int,
    var pci: Int?,
    var rsrq: Int?,
    var sinr: Int?,
    var bandNr: Int,
    var bandName: String? = "?",
    var rxFrequency: Double,
    var txFrequency: Double
) {

    fun getEnbNumber(): Long = cellId / 256

    fun getSector(): Int = (cellId % 256).toInt()

    fun isActive(): Boolean =
        connectionType is PrimaryConnection || connectionType is SecondaryConnection

    fun setActive(active: Boolean) {
        connectionType = if (active) PrimaryConnection() else NoneConnection()
    }


}
