package athila.kforecast.screens.forecast.core

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.app.extensions.bindView
import athila.kforecast.screens.common.BaseView
import athila.kforecast.screens.forecast.core.ForecastContract.Presenter
import athila.kforecast.screens.forecast.core.adapter.CitiesSpinnerAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.utils.ForecastUtils

@SuppressLint("ViewConstructor")
class ForecastView(context: Context, private val forecastAdapter: ForecastAdapter,
    private val citiesSpinnerAdapter: CitiesSpinnerAdapter) : ForecastContract.View, BaseView(context) {

  private lateinit var presenter: ForecastContract.Presenter

  private val screenContent: View by bindView(R.id.weather_screen_content)
  private val emptyView: View by bindView(R.id.weather_screen_empty_view)
  private val forecastList: RecyclerView by bindView(R.id.weather_screen_recyclerView_forecast)
  private val currentConditionsSummary: TextView by bindView(R.id.weather_screen_textView_current_conditions_summary)
  private val currentConditionsIcon: ImageView by bindView(R.id.weather_screen_imageView_current_conditions_icon)
  private val currentConditionsTemperature: TextView by bindView(R.id.weather_screen_textView_current_conditions_temperature)
  private val refreshButton: ImageView by bindView(R.id.forecast_screen_app_bar_button_refresh)
  private val addButton: ImageView by bindView(R.id.forecast_screen_app_bar_button_add)
  private val progressView: ContentLoadingProgressBar by bindView(R.id.forecast_screen_app_bar_progress)
  private val citiesSpinner: Spinner by bindView(R.id.forecast_screen_app_bar_spinner_cities)

  init {
    inflate(context, R.layout.view_forecast, this)
    forecastList.setHasFixedSize(true)
    forecastList.adapter = forecastAdapter
    citiesSpinner.adapter = citiesSpinnerAdapter
    forecastList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    setListeners()
  }

  private fun setListeners() {
    citiesSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        showEmptyView()
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.setSelectedCity(citiesSpinnerAdapter.getItem(position))
      }
    }

    refreshButton.setOnClickListener { presenter.refresh() }

    addButton.setOnClickListener { presenter.insertRandomCity() }
  }

  override fun showEmptyView() {
    emptyView.visibility = View.VISIBLE
    screenContent.visibility = View.GONE
  }

  override fun getSelectedCity(): City? {
    return citiesSpinnerAdapter.getItem(citiesSpinner.selectedItemPosition)
  }

  override fun setCities(cities: List<City>?) {
    citiesSpinnerAdapter.setCities(cities)
  }

  override fun showProgress() {
    refreshButton.visibility = View.GONE
    progressView.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    refreshButton.visibility = View.VISIBLE
    progressView.visibility = View.GONE
  }

  override fun setForecast(forecast: Forecast) {
    emptyView.visibility = View.GONE
    screenContent.visibility = View.VISIBLE
    val currentConditions = forecast.currently
    currentConditionsIcon.setImageDrawable(
        ContextCompat.getDrawable(context, ForecastUtils.getWeatherIcon(currentConditions?.icon)))
    currentConditionsSummary.text = currentConditions?.summary
    currentConditionsTemperature.text = context.getString(R.string.temperature, currentConditions?.temperature.toString())

    forecastAdapter.forecast = forecast
  }

  override fun setPresenter(presenter: Presenter) {
    this.presenter = presenter
  }

  override fun getView(): View = this

  override fun getLifecycleOwner(): LifecycleOwner = context as LifecycleOwner
}