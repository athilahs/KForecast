package athila.kforecast.screens.forecast.utils

import android.support.annotation.DrawableRes
import athila.kforecast.R

object ForecastUtils {
  // These values are in compliance with the API
  private val CLEAR_DAY = "clear-day"
  private val CLEAR_NIGHT = "clear-night"
  private val RAIN = "rain"
  private val SNOW = "snow"
  private val CLOUDY = "cloudy"
  private val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
  private val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"

  @DrawableRes fun getWeatherIcon(weatherCondition: String?): Int {
    return when (weatherCondition) {
      CLEAR_DAY, CLEAR_NIGHT -> R.drawable.ic_weather_clear_day

      RAIN -> R.drawable.ic_weather_rain

      SNOW -> R.drawable.ic_weather_snow

      CLOUDY -> R.drawable.ic_weather_cloudy

      PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> R.drawable.ic_weather_partly_cloudy

      else ->
        // TODO: get a default icon
        R.drawable.ic_weather_clear_day
    }
  }
}