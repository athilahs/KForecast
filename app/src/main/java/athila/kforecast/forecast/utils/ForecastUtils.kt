package athila.kforecast.forecast.utils

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
    when (weatherCondition) {
      CLEAR_DAY, CLEAR_NIGHT -> return R.drawable.ic_weather_clear_day

      RAIN -> return R.drawable.ic_weather_rain

      SNOW -> return R.drawable.ic_weather_snow

      CLOUDY -> return R.drawable.ic_weather_cloudy

      PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> return R.drawable.ic_weather_partly_cloudy

      else ->
        // TODO: get a default icon
        return R.drawable.ic_weather_clear_day
    }
  }
}