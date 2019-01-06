package athila.kforecast.app.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import athila.kforecast.app.database.entity.Forecast

@Dao
interface ForecastDao {
  @Query("select * from " + Forecast.TABLE_NAME + " WHERE cityId = :cityId")
  fun forecastForCity(cityId: Long): LiveData<Forecast>

  @Query("select * from " + Forecast.TABLE_NAME)
  fun getAllForecasts(): LiveData<List<Forecast>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertForecast(forecast: Forecast)

  @Delete
  fun deleteForecast(forecast: Forecast)
}