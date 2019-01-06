package athila.kforecast.screens.forecast.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import athila.kforecast.app.api.await
import athila.kforecast.app.common.AppExecutors
import athila.kforecast.app.common.Resource
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.forecast.api.ForecastApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultForecastRepository(val api: ForecastApi, val dao: ForecastDao) : ForecastRepository {
  private val _forecastForCityLiveData: MediatorLiveData<Resource<Forecast>> = MediatorLiveData<Resource<Forecast>>().apply {
    value = Resource.idle()
  }

  override val forecastForCityLiveData: LiveData<Resource<Forecast>>
    get() = _forecastForCityLiveData

  private lateinit var currentForecastQuery: LiveData<Forecast>


  override suspend fun fetchForecast(city: City) = withContext(AppExecutors.mainThread) {
    Log.e("ATHILA", "ATHILA - fetching forecast for city: $city")
    if (!::currentForecastQuery.isInitialized) {
      // just to bind the changes to database to this livedata
      getLocalForecastForCity(city.id)
    }
    Log.e("ATHILA", "ATHILA - setting LOADING emission to _forecastForCityLiveData. ID: ${currentForecastQuery.value?.id}")
    _forecastForCityLiveData.value = Resource.loading(currentForecastQuery.value)
    try {
      withContext(AppExecutors.bgThread) {
        // The livedata bound from DAO will be responsible for emitting the success status containing the data after the insertion
        val forecastFromApi = api.getForecast(city.latitude, city.longitude).await().apply {
          cityId = city.id
        }
        Log.e("ATHILA", "ATHILA - got forecast from network. ID: ${forecastFromApi.id}, cityId: ${forecastFromApi.cityId}")
        dao.insertForecast(forecastFromApi)
      }
    } catch (error: Throwable) {
      // let the cancellation signal be propagated
      if (error !is CancellationException) {
        _forecastForCityLiveData.value = Resource.error(error)
      }
    }
  }

  // starting in the mainThread context since there are methods called withing this function required to be called in the main
  // thread
  override fun getLocalForecastForCity(cityId: Long) {
    Log.e("ATHILA", "ATHILA - getLocalForecastForCity called. city ID: $cityId")
    if (::currentForecastQuery.isInitialized) {
      Log.e("ATHILA", "ATHILA - currentForecastQuery was already initialized, removing it from forecast source")
      _forecastForCityLiveData.removeSource(currentForecastQuery)
    }

    Log.e("ATHILA", "ATHILA - executing dao.forecastForCity(cityId = $cityId)")
    currentForecastQuery = dao.forecastForCity(cityId)

    _forecastForCityLiveData.addSource<Forecast>(currentForecastQuery) {
      Log.e("ATHILA", "ATHILA - currentForecastQuery changed. new forecast emitted. ID: ${it?.id}, cityId: ${it?.cityId}")
      _forecastForCityLiveData.value = Resource.success(it)
    }
  }

  override suspend fun getForecast(city: City) = withContext(AppExecutors.mainThread) {
    if (::currentForecastQuery.isInitialized) {
      Log.e("ATHILA", "ATHILA - currentForecastQuery was already initialized, removing it from forecast source")
      _forecastForCityLiveData.removeSource(currentForecastQuery)
    }

    Log.e("ATHILA", "ATHILA - executing dao.forecastForCity(cityId = ${city.id})")
    currentForecastQuery = dao.forecastForCity(city.id)

    _forecastForCityLiveData.addSource<Forecast>(currentForecastQuery) {
      Log.e("ATHILA", "ATHILA - currentForecastQuery changed. new forecast emitted. ID: ${it?.id}, cityId: ${it?.cityId}")
      if (it != null) {
        _forecastForCityLiveData.value = Resource.success(it)
      } else {
        // FIXME: how to use client scope here? Try to use LiveData Transformations.map or switchMap
        GlobalScope.launch {
          fetchForecast(city)
        }
      }
    }
  }
}
