package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import athila.kforecast.app.common.Status.ERROR
import athila.kforecast.app.common.Status.IDLE
import athila.kforecast.app.common.Status.LOADING
import athila.kforecast.app.common.Status.SUCCESS
import athila.kforecast.app.database.entity.City
import athila.kforecast.screens.common.BasePresenter
import athila.kforecast.screens.common.repository.CitiesRepository
import java.util.Date

class ForecastPresenter(
    private val forecastView: ForecastContract.View,
    private val forecastViewModel: ForecastContract.ViewModel, private val testCitiesRepository: CitiesRepository) :
    ForecastContract.Presenter,
    BasePresenter() {

  override fun onStart() {
    initCities()
    initForecast()
  }

  private fun initCities() {
    forecastViewModel.citiesLiveData
        .observe(forecastView.getLifecycleOwner(), Observer { cities ->
          forecastView.setCities(cities)
        })
  }

  private fun initForecast() {
    forecastViewModel.forecastLiveData
        .observe(forecastView.getLifecycleOwner(),
            Observer { forecast ->
              Log.e("ATHILA", "new forecast emission to view. status: ${forecast?.status}, " +
                  "updatedAt: ${Date(forecast?.data?.updatedAt ?: 0)}")
              forecast?.let {
                // set forecast data if available no matter what status
                it.data?.apply {
                  forecastView.setForecast(this)
                }

                when (it.status) {
                  LOADING -> {
                    forecastView.showProgress()
                  }
                  SUCCESS, IDLE -> {
                    forecastView.hideProgress()
                  }
                  ERROR -> {
                    handleBasicError(forecastView, it.error)
                    forecastView.hideProgress()
                  }
                }
              }
            })
  }

  override fun setSelectedCity(city: City) {
    forecastViewModel.setSelectedCity(city)
  }

  override fun refresh() {
    forecastViewModel.refreshForecast()
  }

  // test
  override fun insertRandomCity() {
    forecastViewModel.insertRandomCity()
  }
}