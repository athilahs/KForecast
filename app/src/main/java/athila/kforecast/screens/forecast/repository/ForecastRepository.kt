package athila.kforecast.screens.forecast.repository

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Resource
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast

interface ForecastRepository {
  val forecastForCityLiveData: LiveData<Resource<Forecast>>

  fun getLocalForecastForCity(cityId: Long)

  suspend fun fetchForecast(city: City)

  suspend fun getForecast(city: City)
}