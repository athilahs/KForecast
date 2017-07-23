package athila.kforecast.forecast.model

data class DailyForecast(var summary: String?, var icon: String?, var data: List<DataPoint>?)