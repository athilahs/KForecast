package athila.kforecast.forecast.core.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.extensions.bindView
import athila.kforecast.forecast.model.Forecast
import athila.kforecast.forecast.utils.ForecastUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ForecastAdapter(private val context: Context) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

  private var mForecast: Forecast? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_row, parent, false)
    return ForecastViewHolder(view)
  }

  override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
    val forecastItem = mForecast?.getDailyForecasts()?.get(position)
    if (forecastItem != null) {
      // Get the day from timestamp
      val calendar = Calendar.getInstance(TimeZone.getTimeZone(mForecast?.timezone))
      // API returns time in seconds
      calendar.timeInMillis = forecastItem.time * 1000
      val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
      holder.forecastItemDay.text = dayOfWeek
      holder.forecastItemIcon.setImageDrawable(
          ContextCompat.getDrawable(context, ForecastUtils.getWeatherIcon(forecastItem.icon)))
      holder.forecastItemSummary.text = forecastItem.summary
      holder.forecastItemTemperatureMax.text = context.getString(R.string.temperature, forecastItem.temperatureMax)
      holder.forecastItemTemperatureMin.text = context.getString(R.string.temperature, forecastItem.temperatureMin)
    }
  }

  override fun getItemCount(): Int {
    return mForecast?.getDailyForecasts()?.size ?: 0
  }

  fun setForecast(forecast: Forecast) {
    mForecast = forecast
    notifyDataSetChanged()
  }

  class ForecastViewHolder(forecastItemView: View) : RecyclerView.ViewHolder(forecastItemView) {
    val forecastItemDay: TextView by bindView(R.id.forecast_item_day)
    val forecastItemTemperatureMax: TextView by bindView(R.id.forecast_item_temperature_max)
    val forecastItemTemperatureMin: TextView by bindView(R.id.forecast_item_temperature_min)
    val forecastItemSummary: TextView by bindView(R.id.forecast_item_summary)
    val forecastItemIcon: ImageView by bindView(R.id.forecast_item_icon)
  }
}
