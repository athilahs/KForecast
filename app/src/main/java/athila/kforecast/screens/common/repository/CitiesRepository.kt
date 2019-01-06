package athila.kforecast.screens.common.repository

import android.arch.lifecycle.LiveData
import athila.kforecast.app.database.entity.City

interface CitiesRepository {
  suspend fun getCities(): LiveData<List<City>>
  suspend fun insertCity(city: City): Long
  suspend fun deleteCity(city: City)
}