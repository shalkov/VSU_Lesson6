package ru.shalkoff.vsu_lesson6.retrofit

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.shalkoff.vsu_lesson6.models.ApiResponse

class ExampleRetrofit {

    private val disposables = CompositeDisposable()
    private val scheduleApiService = MyRetrofitClient.client.create(
        ScheduleApiService::class.java
    )

    /**
     * Синхронное выполнение запроса
     * (обёрнуто в коуртину для выполнения в фоне)
     */
    fun sendRequest1(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val call = scheduleApiService.getSchedule1("310")
                val response = call.execute()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        Log.d("LESSON6", responseBody.toString())
                    }
                } else {
                    Log.d("LESSON6", "HTTP Error: ${response.code()}")
                }
            }
            Toast.makeText(
                activity,
                "#6 Запрос выполнился (Retrofit execute)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendRequest2(context: Context) {
        val call = scheduleApiService.getSchedule1("310")
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                val responseBody = response.body()
                responseBody?.let {
                    Log.d("LESSON6", responseBody.toString())
                }
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "#7 Запрос выполнился (Retrofit enqueue)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("LESSON6", "Error: ${t.message}")
            }
        })
    }

    fun sendRequest3(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = scheduleApiService.getSchedule2("310")
                    Log.d("LESSON6", response.toString())
                } catch (e: HttpException) {
                    Log.d("LESSON6", "HTTP Error: ${e.code()}")
                } catch (e: Exception) {
                    Log.d("LESSON6", "Error: ${e.message}")
                }
            }

            Toast.makeText(
                activity,
                "#8 Запрос выполнился (Retrofit корутина)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendRequest4(context: Context) {
        disposables.clear()

        val disposable = scheduleApiService.getSchedule3("310")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Log.d("LESSON6", response.toString())
                Toast.makeText(
                    context,
                    "#9 Запрос выполнился (Retrofit RxJava)",
                    Toast.LENGTH_SHORT
                ).show()
            }, { error ->
                if (error is HttpException) {
                    Log.d("LESSON6", "HTTP Error: ${error.code()}")
                } else {
                    Log.d("LESSON6", "Error: ${error.message}")
                }
            })
        disposables.add(disposable)
    }

    fun clearDisposables() {
        disposables.clear()
    }
}