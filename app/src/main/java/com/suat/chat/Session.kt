package com.suat.chat

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

public data class Session(
    @SerializedName("kID") var kID: Int?,
    @SerializedName("kAdi") var kAdi: String?,
    @SerializedName("kSifre") var kSifre: String?,
    @SerializedName("kFoto") var kFoto: String?
): Serializable
