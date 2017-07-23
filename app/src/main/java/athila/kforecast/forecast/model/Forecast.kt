package athila.kforecast.forecast.model

data class Forecast(var timezone: String?, var latitude: Double = 0.0, var longitude: Double = 0.0, var currently: DataPoint?,
    var daily: DailyForecast?) {

  fun getDailyForecasts(): List<DataPoint>? = daily?.data
}