package com.suat.chat

import com.google.gson.annotations.SerializedName

public data class Mesajlar(
    @SerializedName("kID") val kID: Int?,
    @SerializedName("kAdi") val kAdi: String?,
    @SerializedName("Icerik") val Icerik: String?,
    @SerializedName("Tarih") val Tarih: String?
)
