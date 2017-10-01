package athila.kforecast.screens.forecast.core.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.extensions.bindView
import athila.kforecast.screens.forecast.core.ForecastContract

class ForecastViewHolder(forecastItemView: View) : RecyclerView.ViewHolder(forecastItemView), ForecastContract.ItemView {
  private val forecastItemDay: TextView by bindView(R.id.forecast_item_day)
  private val forecastItemTemperatureMax: TextView by bindView(R.id.forecast_item_temperature_max)
  private val forecastItemTemperatureMin: TextView by bindView(R.id.forecast_item_temperature_min)
  private val forecastItemSummary: TextView by bindView(R.id.forecast_item_summary)
  private val forecastItemIcon: ImageView by bindView(R.id.forecast_item_icon)

  override fun dayOfWeek(dayOfWeek: String) {
    forecastItemDay.text = dayOfWeek
  }

  override fun weatherIcon(icon: Int) {
    forecastItemIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, icon))
  }

  override fun summary(summary: String) {
    forecastItemSummary.text = summary
  }

  override fun maxTemperature(maxTemperature: Double) {
    forecastItemTemperatureMax.text = itemView.context.getString(R.string.temperature, maxTemperature)
  }

  override fun minTemperature(minTemperature: Double) {
    forecastItemTemperatureMin.text = itemView.context.getString(R.string.temperature, minTemperature)
  }
}