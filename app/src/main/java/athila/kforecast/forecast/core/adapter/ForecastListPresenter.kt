package athila.kforecast.forecast.core.adapter

import athila.kforecast.forecast.core.ForecastContract
import athila.kforecast.forecast.model.Forecast
import athila.kforecast.forecast.utils.ForecastUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ForecastListPresenter : ForecastContract.AdapterPresenter {
  private var mForecast: Forecast? = null

  override fun itemsCount(): Int = mForecast?.getDailyForecasts()?.size ?: 0

  override fun onBindItemAtPosition(position: Int, view: ForecastContract.ItemView) {
    val forecastItem = mForecast?.getDailyForecasts()?.get(position)
    if (forecastItem != null) {
      // Get the day from timestamp
      val calendar = Calendar.getInstance(TimeZone.getTimeZone(mForecast?.timezone))
      // API returns time in seconds
      calendar.timeInMillis = forecastItem.time * 1000
      val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
      view.dayOfWeek(dayOfWeek)
      view.weatherIcon(ForecastUtils.getWeatherIcon(forecastItem.icon))
      view.summary(forecastItem.summary ?: "???")
      view.maxTemperature(forecastItem.temperatureMax)
      view.minTemperature(forecastItem.temperatureMin)
    }
  }

  override fun setData(data: Forecast?) {
    mForecast = data
  }

}