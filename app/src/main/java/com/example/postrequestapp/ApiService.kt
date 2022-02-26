package com.example.postrequestapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("qrgen")
    suspend fun generate(@Part filePart: MultipartBody.Part,
                         @Query("colored") colored: Boolean,
                         @Query("content")  content: String): JsonExample

}