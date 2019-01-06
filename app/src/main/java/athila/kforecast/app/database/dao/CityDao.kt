package athila.kforecast.app.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import athila.kforecast.app.database.entity.City

@Dao
interface CityDao {
  @Query("select * from " + City.TABLE_NAME)
  fun getAllCities(): LiveData<List<City>>

  @Insert(onConflict = REPLACE)
  fun insertCity(city: City): Long

  @Delete
  fun deleteCity(city: City)
}