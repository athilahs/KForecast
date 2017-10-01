package athila.kforecast.app.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import athila.kforecast.app.database.entity.City
import io.reactivex.Flowable

@Dao
interface CityDao {
  @Query("select * from " + City.TABLE_NAME)
  fun getAllCities(): Flowable<List<City>>

  @Insert(onConflict = REPLACE)
  fun insertCity(city: City)

  @Delete
  fun deleteCity(city: City)
}