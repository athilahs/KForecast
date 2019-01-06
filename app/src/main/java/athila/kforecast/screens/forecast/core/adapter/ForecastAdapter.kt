package athila.kforecast.screens.forecast.core.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.app.extensions.bindView
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.ForecastContract.AdapterPresenter

class ForecastAdapter(private val presenter: AdapterPresenter) : RecyclerView.Adapter<ForecastViewHolder>() {

  var forecast: Forecast? = null
    set(value) {
      field = value
      presenter.setData(value)
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_row, parent, false)
    return ForecastViewHolder(view)
  }

  override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
    presenter.onBindItemAtPosition(position, holder)
  }

  override fun getItemCount(): Int {
    return forecast?.daily?.data?.size ?: 0
  }
}

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
    forecastItemTemperatureMax.text = itemView.context.getString(R.string.temperature, maxTemperature.toString())
  }

  override fun minTemperature(minTemperature: Double) {
    forecastItemTemperatureMin.text = itemView.context.getString(R.string.temperature, minTemperature.toString())
  }
}
