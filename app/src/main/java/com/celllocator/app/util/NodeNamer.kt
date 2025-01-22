package com.celllocator.app.util

import cz.mroczis.netmonster.core.db.model.NetworkType
import java.util.Locale

val networkTypes = mapOf(
    NetworkType.Gsm::class to "Cell",
    NetworkType.Wcdma::class to "NB",
    NetworkType.Lte::class to "eNB",
    NetworkType.Nr::class to "gNB",
    NetworkType.Unknown::class to "Unknown"
)

val nodeNames = mapOf(
    "GSM" to "Cell",
    "UMTS" to "NB",
    "LTE" to "eNB",
    "NR" to "gNB",
    "5G" to "gNB",
    "NR SA" to "gNB",
)

fun getNodeName(networkType: NetworkType): String {
    return networkTypes[networkType::class] ?: "Unknown"
}

fun getNodeName(networkType: String): String {
    // ignore case of networkType
    return nodeNames[networkType.uppercase(Locale.getDefault())] ?: "Unknown"
}