package com.example.postrequestapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.postrequestapp.databinding.ActivityMainBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = initApi()

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri = it
            binding.pathField.text = uri.toString()
        }

        binding.pickImageButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.requestButton.setOnClickListener {
            val file = File(uri.path!!)
            val formData = MultipartBody.Part.createFormData(
                "file",
                file.absolutePath,
                file.asRequestBody("multipart/form-data".toMediaType())
            )

            api.generate(formData).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful)
                        Log.i("loger", response.body().toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("loger", t.message.toString())
                }


            })
        }


    }

    private fun initApi(): ApiService {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    companion object {
        private const val baseUrl = "https://qrcone.herokuapp.com/"
    }


}