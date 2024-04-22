package ru.shalkoff.vsu_lesson6.models

data class TimeItem(
    val id: Int,
    val time: String,
    val description: String
)

data class Departure(
    val id: Int,
    val departureFrom: String,
    val timeList: List<TimeItem>
)

data class Route(
    val id: Int,
    val routeNumber: String,
    val name: String,
    val description: String,
    val departureStart: Departure,
    val departureEnd: Departure
)

data class Info(
    val status: String,
    val message: String
)

data class ApiResponse(
    val route: Route? = null,
    val info: Info? = null
) {

    companion object {

        val EMPTY = ApiResponse(null, null)
    }
}

/**
 * Использовать этот объект для выполнения практической работы
 */
data class RoutesResponse(
    val routes: List<Route>,
    val info: Info
)