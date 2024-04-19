package ru.shalkoff.vsu_lesson6.okhttp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object MyOkHttpClient {

    val client: OkHttpClient = createHttpClient()

    private fun createHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        // Уровень логирования BODY для вывода полных данных запроса и ответа
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            // Добавляем интерсептор для логирования
            .addInterceptor(loggingInterceptor)
            .build()
    }
}