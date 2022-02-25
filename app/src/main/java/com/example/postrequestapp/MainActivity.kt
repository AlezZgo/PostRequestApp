package com.example.postrequestapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.example.postrequestapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var path: String
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = initApi()

        val config = ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
        }

        val launcher = registerImagePicker {
            path = it.first().path
        }

        binding.pickImageButton.setOnClickListener {
            launcher.launch(config)
        }

        binding.requestButton.setOnClickListener {

            CoroutineScope(Dispatchers.IO + Job()).launch {
                generate()
            }


        }


    }


    private suspend fun generate() {

        val file = File(path)

        val formData = MultipartBody.Part.createFormData(
            "file",
            file.absolutePath,
            file.asRequestBody("multipart/form-data".toMediaType())
        )


        val response = api.generate(formData,false,"Artem pidor").filename

//        val imageBytes = Base64.decode(responseBase64, Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        runOnUiThread {
//            binding.imageView2.setImageBitmap(bitmap)
            Log.i("loger",response)
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