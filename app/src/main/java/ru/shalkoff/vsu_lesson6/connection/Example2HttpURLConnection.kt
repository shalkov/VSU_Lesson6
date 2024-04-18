package ru.shalkoff.vsu_lesson6.connection

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import ru.shalkoff.vsu_lesson6.Const.API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse
import ru.shalkoff.vsu_lesson6.models.Departure
import ru.shalkoff.vsu_lesson6.models.Info
import ru.shalkoff.vsu_lesson6.models.Route
import ru.shalkoff.vsu_lesson6.models.TimeItem
import java.io.BufferedReader
import java.io.InputStreamReader
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
        // Создаем Handler, связанный с основным (UI) потоком
        val mainHandler = Handler(Looper.getMainLooper())

        // Запуск операции в фоновом потоке с помощью Thread
        Thread {
            val result = request() ?: ApiResponse.EMPTY

            // Отображаем результат в основной (UI) поток с помощью mainHandler
            mainHandler.post {
                // Показать результат на UI, например, с помощью Toast
                Toast.makeText(context, "#2 Запрос выполнился", Toast.LENGTH_SHORT).show()
                Log.d("LESSON6", result.toString())
            }
        }.start()
    }

    private fun request(): ApiResponse? {
        var apiResponse: ApiResponse? = null
        val url = URL(API_URL)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        val responseCode = conn.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Чтение и обработка ответа
            val inputStream = conn.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            val responseBody = response.toString()

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