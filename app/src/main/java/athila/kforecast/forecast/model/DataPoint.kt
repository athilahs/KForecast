package athila.kforecast.forecast.model

data class DataPoint(var time: Long = 0, var summary: String?, var icon: String?, var temperatureMin: Double, var
temperatureMax: Double, var temperature: Double)