package com.example.postrequestapp

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class JsonExample  (
    @SerializedName("file")
    val filename: String,
//    @SerializedName("description")
//    val description: Description,

)