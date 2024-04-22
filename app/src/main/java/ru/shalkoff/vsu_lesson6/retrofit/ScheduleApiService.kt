package ru.shalkoff.vsu_lesson6.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.shalkoff.vsu_lesson6.Const.API_URL
import ru.shalkoff.vsu_lesson6.models.ApiResponse

interface ScheduleApiService {

    @GET(API_URL)
    fun getSchedule1(
        @Path("routeId") routeId: String
    ): Call<ApiResponse>

    @GET(API_URL)
    suspend fun getSchedule2(
        @Path("routeId") routeId: String
    ): ApiResponse

    @GET(API_URL)
    fun getSchedule3(
        @Path("routeId") routeId: String
    ): Observable<ApiResponse>
}