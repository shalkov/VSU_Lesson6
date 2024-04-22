package ru.shalkoff.vsu_lesson6.okhttp

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import ru.shalkoff.vsu_lesson6.Const.FULL_API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse

class Example1OkHttp {

    private val gson = Gson()

    fun sendRequest(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url(FULL_API_URL)
                    .build()
                val response = MyOkHttpClient.client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""

                // Десериализация JSON-строки в объект с помощью Gson
                val responseObject = gson.fromJson(responseBody, ApiResponse::class.java)

                Log.d("LESSON6", responseObject.toString())
            }
            Toast.makeText(activity, "#4 Запрос выполнился (OkHttp execute)", Toast.LENGTH_SHORT).show()
        }
    }
}