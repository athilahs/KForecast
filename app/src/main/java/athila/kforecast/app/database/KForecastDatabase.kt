package athila.kforecast.app.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast

@Database(entities = [City::class, Forecast::class], version = 1, exportSchema = false)
abstract class KForecastDatabase : RoomDatabase() {

  abstract fun cityDao(): CityDao
  abstract fun forecastDao(): ForecastDao
}