package com.celllocator.app.enums

enum class NetworkType(val value: Int) {
    Unknown(0),
    Cdma(1),
    Gsm(2),
    Wcdma(3),
    Lte(4),
    Nr(5),
    Tdscdma(6);

    companion object {
        fun fromValue(value: Int): NetworkType {
            return when (value) {
                1 -> Cdma
                2 -> Gsm
                3 -> Wcdma
                4 -> Lte
                5 -> Nr
                6 -> Tdscdma
                0 -> Unknown
                else -> Unknown
            }
        }
    }
}
