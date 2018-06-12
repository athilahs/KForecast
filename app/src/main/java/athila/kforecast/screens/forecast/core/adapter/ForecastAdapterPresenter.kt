package athila.kforecast.screens.forecast.core.adapter

import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.utils.ForecastUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ForecastAdapterPresenter : ForecastContract.AdapterPresenter {
  private var mForecast: Forecast? = null

  override fun itemsCount(): Int = mForecast?.daily?.data?.size ?: 0

  override fun onBindItemAtPosition(position: Int, view: ForecastContract.ItemView) {
    val forecastItem = mForecast?.daily?.data?.get(position)
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