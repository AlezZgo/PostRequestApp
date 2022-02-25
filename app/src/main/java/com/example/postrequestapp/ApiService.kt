package com.example.postrequestapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("qrgen")
    suspend fun generate(@Part filePart: MultipartBody.Part,
                         @Part("colored") colored: Boolean,
                         @Part("content")  content: String): JsonExample

}