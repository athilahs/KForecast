package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.Observer
import android.os.Bundle
import athila.kforecast.screens.common.BasePresenter
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase.GetForecastParams

class ForecastPresenter(
    private val forecastView: ForecastContract.View,
    private val forecastViewModel: ForecastContract.ViewModel) : ForecastContract.Presenter, BasePresenter() {

  override fun onCreate(savedInstanceState: Bundle?) {
    initCities()
    initForecast()
  }

  override fun onStart() {
    subscribeToCitiesSelection()

  }

  private fun subscribeToCitiesSelection() {
    forecastView.observeCityChanges()
        .subscribe { city ->
          if (city == null) {
            forecastView.showEmptyView()
          } else {
            forecastViewModel.switchCity(GetForecastParams(city))
          }
        }
  }

  private fun initCities() {
    forecastViewModel.getCities()
        .observe(forecastView.getLifecycleOwner(), Observer { result ->
          forecastView.setCities(result?.data)
          // TODO: need to find a way to init (get initial) forecast and subscribe for cities changes only after cities are full
          // TODO: initialised (either from db or from server if needed). Maybe a flag "initialised" is the best way
        })
  }

  private fun initForecast() {
    forecastViewModel.getCurrentForecast(GetForecastParams(forecastView.getSelectedCity()))
        .observe(forecastView.getLifecycleOwner(),
            Observer { result ->
              if (result == null || !result.isSuccess()) {
                handleBasicError(forecastView, result?.error)
                forecastView.showEmptyView()
              } else if (result.data == null) {
                forecastView.showEmptyView()
              } else {
                forecastView.setForecast(result.data)
              }
            })
  }

  private fun setCurrentForecast() {

  }
}