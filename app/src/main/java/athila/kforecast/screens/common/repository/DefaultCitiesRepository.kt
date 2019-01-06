package athila.kforecast.screens.common.repository

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.AppExecutors
import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.database.entity.City
import kotlinx.coroutines.withContext

class DefaultCitiesRepository(val dao: CityDao) : CitiesRepository {
  override suspend fun getCities(): LiveData<List<City>> = withContext(AppExecutors.bgThread) {
    dao.getAllCities()
  }

  override suspend fun insertCity(city: City): Long = withContext(AppExecutors.bgThread) {
    dao.insertCity(city)
  }

  override suspend fun deleteCity(city: City) = withContext(AppExecutors.bgThread) {
    dao.deleteCity(city)
  }
}