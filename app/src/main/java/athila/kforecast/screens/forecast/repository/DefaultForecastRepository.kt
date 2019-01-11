package athila.kforecast.screens.forecast.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import athila.kforecast.app.api.ApiResponse
import athila.kforecast.app.common.NetworkBoundResource
import athila.kforecast.app.common.Resource
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.forecast.api.ForecastApi
import kotlinx.coroutines.CoroutineScope
import java.util.Date

class DefaultForecastRepository(val api: ForecastApi, val dao: ForecastDao) : ForecastRepository {
  private val _forecastForCityLiveData: MediatorLiveData<Resource<Forecast>> = MediatorLiveData<Resource<Forecast>>().apply {
    value = Resource.idle()
  }

  override val forecastForCityLiveData: LiveData<Resource<Forecast>>
    get() = _forecastForCityLiveData


  override fun getForecast(city: City, forceUpdate: Boolean, coroutineScope: CoroutineScope): LiveData<Resource<Forecast>> {
    return object : NetworkBoundResource<Forecast, Forecast>() {
      override val coroutineScope: CoroutineScope
        get() = coroutineScope

      override fun saveCallResult(item: Forecast) {
        item.cityId = city.id
        item.updatedAt = System.currentTimeMillis()
        Log.e("ATHILA", "inserting new forecast into DB. updatedAt: ${Date(item.updatedAt)}")
        dao.insertForecast(item)
      }

      // TODO: implement expiration logic
      override fun shouldFetch(data: Forecast?): Boolean = forceUpdate || data == null

      override fun loadFromDb(): LiveData<Forecast> = dao.forecastForCity(city.id)

      override fun createCall(): LiveData<ApiResponse<Forecast>> = api.getForecast(city.latitude, city.longitude)

    }.asLiveData()
  }
}
