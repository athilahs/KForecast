package athila.kforecast.screens.forecast.core.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import athila.kforecast.R
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.forecast.core.ForecastContract.AdapterPresenter

class ForecastAdapter(private val mPresenter: AdapterPresenter) : RecyclerView.Adapter<ForecastViewHolder>() {

  var forecast: Forecast?
    get() = forecast
    set(value) {
      forecast = value
      mPresenter.setData(value)
      notifyDataSetChanged()
    }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_row, parent, false)
    return ForecastViewHolder(view)
  }

  override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
    mPresenter.onBindItemAtPosition(position, holder)
  }

  override fun getItemCount(): Int {
    return forecast?.daily?.data?.size ?: 0
  }
}
