package ru.shalkoff.vsu_lesson6.connection

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.shalkoff.vsu_lesson6.Const.API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse
import java.net.HttpURLConnection
import java.net.URL

/**
 * Пример с осуществлением запроса с помощью HttpURLConnection
 * 1. Для создания фонового потока использует RxKotlin и оператор: .subscribeOn(Schedulers.io())
 * 2. Результат парсится с помощью Gson
 * 3. Для отображения результата на UI используется оператор: .observeOn(AndroidSchedulers.mainThread())
 */
class Example3HttpURLConnection {

    private val disposables = CompositeDisposable()

    fun sendRequest(context: Context) {
        disposables.clear()
        disposables.add(Observable.fromCallable {
            var apiResponse: ApiResponse? = null
            // Выполнение операции в фоновом потоке
            val url = URL(API_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Чтение и обработка ответа
                val inputStream = conn.inputStream
                val responseBody = inputStream.bufferedReader().use { it.readText() }

                // Преобразование JSON-строки в объект ApiResponse
                val gson = Gson()
                apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
            }
            return@fromCallable apiResponse ?: ApiResponse.EMPTY
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Переключаемся на главный поток для обработки результата
            .subscribe { apiResponse ->
                // Обработка результата в UI потоке
                Log.d("LESSON6", apiResponse.toString())
                Toast.makeText(context, "#3 Запрос выполнился", Toast.LENGTH_SHORT).show()
            })
    }

    fun clearDisposables() {
        disposables.clear()
    }
}