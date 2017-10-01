package athila.kforecast.app.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import athila.kforecast.app.database.entity.Forecast
import io.reactivex.Flowable

@Dao
interface ForecastDao {
  @Query("select * from " + Forecast.TABLE_NAME + " WHERE cityId = :cityId")
  fun forecastForCity(cityId: Long): Flowable<Forecast>

  @Query("select * from " + Forecast.TABLE_NAME)
  fun getAllForecasts(cityId: Long): Flowable<List<Forecast>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertForecast(forecast: Forecast)

  @Delete
  fun deleteForecast(forecast: Forecast)
}