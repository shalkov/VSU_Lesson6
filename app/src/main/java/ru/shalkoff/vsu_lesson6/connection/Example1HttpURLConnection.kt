package ru.shalkoff.vsu_lesson6.connection

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shalkoff.vsu_lesson6.Const.API_URL
import java.net.HttpURLConnection
import java.net.URL

/**
 * Пример с осуществлением запроса с помощью HttpURLConnection
 * 1. Использует Корутина с привязкой к жизненному циклу Activity.
 * 2. Для выполнения запроса в фоне используется корутина Dispatchers.IO
 * 3. Результат из боди формируется в строку (преобразования в объект - нет)
 * 4. Для отображения результата на UI используется корутина из lifecycleScope (она работает на UI потоке)
 */
class Example1HttpURLConnection {

    fun sendRequest(activity: AppCompatActivity) {
        // Запуск кода в фоновом потоке
        activity.lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                request()
            }
            Toast.makeText(activity, "#1 Запрос выполнился", Toast.LENGTH_SHORT).show()
            Log.d("LESSON6", result ?: "NULL")
        }
    }

    private fun request(): String? {
        val url = URL(API_URL)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        val responseCode = conn.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = conn.inputStream
            val response = inputStream.bufferedReader().use {
                it.readText()
            }
            return response
        }
        return null
    }
}