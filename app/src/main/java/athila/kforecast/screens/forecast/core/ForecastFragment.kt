package athila.kforecast.screens.forecast.core

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.KForecastApplication
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.app.extensions.bindView
import athila.kforecast.screens.common.BasicErrorsHandlerView
import athila.kforecast.screens.common.widget.RelativeTimeTextView
import athila.kforecast.screens.forecast.core.adapter.CitiesSpinnerAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.di.DaggerForecastComponent
import athila.kforecast.screens.forecast.di.ForecastModule
import athila.kforecast.screens.forecast.utils.ForecastUtils
import java.net.UnknownHostException
import javax.inject.Inject

class ForecastFragment : Fragment(), ForecastContract.View, BasicErrorsHandlerView {
  @Inject
  lateinit var forecastPresenter: ForecastContract.Presenter

  @Inject
  lateinit var forecastAdapter: ForecastAdapter

  @Inject
  lateinit var citiesSpinnerAdapter: CitiesSpinnerAdapter

  private val screenContent: View by bindView(R.id.weather_screen_content)

  private lateinit var rootView: View

  private val emptyView: View by bindView(R.id.weather_screen_empty_view)
  private val forecastList: RecyclerView by bindView(R.id.weather_screen_recyclerView_forecast)
  private val currentConditionsSummary: TextView by bindView(R.id.weather_screen_textView_current_conditions_summary)
  private val currentConditionsIcon: ImageView by bindView(R.id.weather_screen_imageView_current_conditions_icon)
  private val currentConditionsTemperature: TextView by bindView(R.id.weather_screen_textView_current_conditions_temperature)
  private val refreshButton: ImageView by bindView(R.id.forecast_screen_app_bar_button_refresh)
  private val addButton: ImageView by bindView(R.id.forecast_screen_app_bar_button_add)
  private val progressView: ContentLoadingProgressBar by bindView(R.id.forecast_screen_app_bar_progress)
  private val lastUpdated: RelativeTimeTextView by bindView(R.id.weather_screen_textView_last_updated)
  private val citiesSpinner: Spinner by bindView(R.id.forecast_screen_app_bar_spinner_cities)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerForecastComponent.builder()
        .applicationComponent((activity?.application as KForecastApplication).getApplicationComponent())
        .forecastModule(ForecastModule(this))
        .build()
        .inject(this)
  }


  @SuppressLint("InflateParams")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.view_forecast, null)
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    forecastList.setHasFixedSize(true)
    forecastList.adapter = forecastAdapter
    citiesSpinner.adapter = citiesSpinnerAdapter
    forecastList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    setListeners()
    forecastPresenter.onStart()
  }

  private fun setListeners() {
    citiesSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>?) {
        showEmptyView()
      }

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        forecastPresenter.setSelectedCity(citiesSpinnerAdapter.getItem(position))
      }
    }

    refreshButton.setOnClickListener { forecastPresenter.refresh() }

    addButton.setOnClickListener { forecastPresenter.insertRandomCity() }
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
        // TODO: improve this !!
        ContextCompat.getDrawable(context!!, ForecastUtils.getWeatherIcon(currentConditions?.icon)))
    currentConditionsSummary.text = currentConditions?.summary
    currentConditionsTemperature.text = context?.getString(R.string.temperature, currentConditions?.temperature.toString())

    lastUpdated.setReferenceTime(forecast.updatedAt)
    forecastAdapter.forecast = forecast
  }

  override fun getLifecycleOwner(): LifecycleOwner = this

  override fun handleBasicError(error: Throwable?): Boolean {

    // TODO: make this generic

    val baseContainer = rootView.findViewById<CoordinatorLayout>(R.id.forecast_base_container)
    val errorMessage = Snackbar.make(baseContainer, R.string.error_generic, Snackbar.LENGTH_LONG)
    if (error is UnknownHostException) {
      errorMessage.setText(R.string.error_no_internet_connection)
    }
    // TODO: add more generic error handling
    // default will show the most generic message
    errorMessage.show()
    return true
  }
}
