package com.example.postrequestapp

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("qrgen")
    fun generate(@Part filePart: MultipartBody.Part): Call<ResponseBody>
}