package ru.shalkoff.vsu_lesson6.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.shalkoff.vsu_lesson6.Const.BASE_URL
import ru.shalkoff.vsu_lesson6.okhttp.MyOkHttpClient

object MyRetrofitClient {

    val client: Retrofit = getRetrofitClient()

    private fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(MyOkHttpClient.client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}