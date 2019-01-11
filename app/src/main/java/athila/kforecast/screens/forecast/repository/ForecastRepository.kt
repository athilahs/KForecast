package athila.kforecast.screens.forecast.repository

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Resource
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

interface ForecastRepository {
  val forecastForCityLiveData: LiveData<Resource<Forecast>>

//  fun getLocalForecastForCity(cityId: Long)
//
//  suspend fun fetchForecast(city: City)
//
//  suspend fun getForecast(city: City)

  fun getForecast(city: City, forceUpdate: Boolean = false,
      coroutineScope: CoroutineScope = GlobalScope): LiveData<Resource<Forecast>>
}