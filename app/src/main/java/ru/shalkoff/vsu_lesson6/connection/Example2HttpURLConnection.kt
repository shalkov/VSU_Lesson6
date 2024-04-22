package ru.shalkoff.vsu_lesson6.connection

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import ru.shalkoff.vsu_lesson6.Const.FULL_API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse
import ru.shalkoff.vsu_lesson6.models.Departure
import ru.shalkoff.vsu_lesson6.models.Info
import ru.shalkoff.vsu_lesson6.models.Route
import ru.shalkoff.vsu_lesson6.models.TimeItem
import java.net.HttpURLConnection
import java.net.URL

/**
 * Пример с осуществлением запроса с помощью HttpURLConnection
 * 1. Для создания фонового потока использует Thread
 * 2. Результат парсится в ручную с помощью JSONObject
 * 3. Для отображения результата на UI используется Handler
 */
class Example2HttpURLConnection {

    fun sendRequest(context: Context) {

        // Запуск операции в фоновом потоке с помощью Thread
        Thread {
            val result = request() ?: ApiResponse.EMPTY

            // Отображаем результат в основной (UI) поток с помощью Handler
            Handler(Looper.getMainLooper()).post {
                // Показать результат на UI, например, с помощью Toast
                Toast.makeText(context, "#2 Запрос выполнился", Toast.LENGTH_SHORT).show()
                Log.d("LESSON6", result.toString())
            }
        }.start()
    }

    private fun request(): ApiResponse? {
        var apiResponse: ApiResponse? = null
        val url = URL(FULL_API_URL)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        val responseCode = conn.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Чтение и обработка ответа
            val inputStream = conn.inputStream
            val responseBody = inputStream.bufferedReader().use {
                it.readText()
            }

            // Парсинг JSON-строки и создание объектов вручную
            val jsonResponse = JSONObject(responseBody)
            val routeObject = jsonResponse.getJSONObject("route")
            val route = Route(
                routeObject.getInt("id"),
                routeObject.getString("routeNumber"),
                routeObject.getString("name"),
                routeObject.getString("description"),
                parseDeparture(routeObject.getJSONObject("departureStart")),
                parseDeparture(routeObject.getJSONObject("departureEnd"))
            )
            val infoObject = jsonResponse.getJSONObject("info")
            val info = Info(
                infoObject.getString("status"),
                infoObject.getString("message")
            )
            apiResponse = ApiResponse(route, info)
        }
        return apiResponse
    }

    private fun parseDeparture(departureObject: JSONObject): Departure {
        val timeListArray = departureObject.getJSONArray("timeList")
        val timeList = mutableListOf<TimeItem>()
        for (i in 0 until timeListArray.length()) {
            val timeItemObject = timeListArray.getJSONObject(i)
            val timeItem = TimeItem(
                timeItemObject.getInt("id"),
                timeItemObject.getString("time"),
                timeItemObject.getString("description")
            )
            timeList.add(timeItem)
        }
        return Departure(
            departureObject.getInt("id"),
            departureObject.getString("departureFrom"),
            timeList
        )
    }
}