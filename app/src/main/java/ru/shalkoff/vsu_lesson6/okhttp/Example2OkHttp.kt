package ru.shalkoff.vsu_lesson6.okhttp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import ru.shalkoff.vsu_lesson6.Const.API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse
import java.io.IOException

class Example2OkHttp {

    fun sendRequest(
        context: Context,
        onSuccess: ((ApiResponse) -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        val request = Request.Builder()
            .url(API_URL)
            .build()
        MyOkHttpClient.client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                onError?.invoke()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""

                // Десериализация JSON-строки в объект с помощью Gson
                val gson = Gson()
                val responseObject = gson.fromJson(responseBody, ApiResponse::class.java)

                onSuccess?.invoke(responseObject)
            }
        })
    }
}