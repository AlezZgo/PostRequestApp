package com.example.postrequestapp

import com.google.gson.annotations.SerializedName
import java.util.*

data class Description (
    @SerializedName("colored") val colored : Boolean,
    @SerializedName("content") val url : String,
    @SerializedName("fileb64") val fileb64 : String
)