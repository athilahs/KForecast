package athila.kforecast.forecast.core

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.common.BaseView
import athila.kforecast.app.extensions.bindView
import athila.kforecast.forecast.core.adapter.ForecastAdapter
import athila.kforecast.forecast.model.Forecast
import athila.kforecast.forecast.utils.ForecastUtils
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import javax.inject.Inject

class ForecastView(context: Context) : ForecastContract.View, BaseView(context) {

  val screenContent: View by bindView(R.id.weather_screen_content)
  val emptyView: View by bindView(R.id.weather_screen_empty_view)
  val forecastList: RecyclerView by bindView(R.id.weather_screen_recyclerView_forecast)
  val currentConditionsSummary: TextView by bindView(R.id.weather_screen_textView_current_conditions_summary)
  val currentConditionsIcon: ImageView by bindView(R.id.weather_screen_imageView_current_conditions_icon)
  val currentConditionsTemperature: TextView by bindView(R.id.weather_screen_textView_current_conditions_temperature)
  val refreshButton: ImageView by bindView(R.id.forecast_screen_app_bar_button_refresh)
  val progressView: ContentLoadingProgressBar by bindView(R.id.forecast_screen_app_bar_progress)

  @Inject
  lateinit var forecastAdapter: ForecastAdapter

  init {
    inflate(context, R.layout.view_forecast, this)
    forecastList.setHasFixedSize(true)
    forecastList.adapter = forecastAdapter
    forecastList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
  }

  override fun showEmptyView() {
    emptyView.visibility = View.VISIBLE
    screenContent.visibility = View.GONE
  }

  override fun observeCityChanges(): Observable<String> {
    return Observable.never()
  }

  override fun showProgress() {
    refreshButton.visibility = View.GONE
    progressView.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    refreshButton.visibility = View.VISIBLE
    progressView.visibility = View.GONE
  }

  override fun observeRefreshButton(): Observable<Any> = RxView.clicks(refreshButton)

  override fun setForecast(forecast: Forecast) {
    emptyView.visibility = View.GONE
    screenContent.visibility = View.VISIBLE
    val currentConditions = forecast.currently
    currentConditionsIcon.setImageDrawable(
        ContextCompat.getDrawable(context, ForecastUtils.getWeatherIcon(currentConditions?.icon)))
    currentConditionsSummary.text = currentConditions?.summary
    currentConditionsTemperature.text = context.getString(R.string.temperature, currentConditions?.temperature)

    forecastAdapter.forecast = forecast
  }

  override fun getView(): View = this
}