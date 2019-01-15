package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Resource
import athila.kforecast.app.common.Status
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.common.BaseAdapterPresenter
import athila.kforecast.screens.common.BasePresenterContract
import athila.kforecast.screens.common.BaseViewContract
import athila.kforecast.screens.common.HasLifecycleOwner


interface ForecastContract {
  interface View : BaseViewContract, HasLifecycleOwner {
//    fun setPresenter(presenter: Presenter)
    fun showEmptyView()
    fun setForecast(forecast: Forecast)
    fun showProgress()
    fun hideProgress()
    fun setCities(cities: List<City>?)
    fun getSelectedCity(): City?
  }

  interface Presenter : BasePresenterContract {
    fun setSelectedCity(city: City)
    fun refresh()

    // test
    fun insertRandomCity()
  }

  interface AdapterPresenter : BaseAdapterPresenter<Forecast, ItemView>

  interface ItemView {
    fun dayOfWeek(dayOfWeek: String)
    fun weatherIcon(icon: Int)
    fun summary(summary: String)
    fun maxTemperature(maxTemperature: Double)
    fun minTemperature(minTemperature: Double)
  }

  interface ViewModel {
    val citiesLiveData: LiveData<List<City>>
    var forecastLiveData: LiveData<Resource<Forecast>>

    fun setSelectedCity(city: City)
    fun refreshForecast()

    // test
    fun insertRandomCity()
  }
}