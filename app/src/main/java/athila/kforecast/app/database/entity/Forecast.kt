package athila.kforecast.app.database.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity
data class Forecast(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ForeignKey(entity = City::class, parentColumns = arrayOf("uid"), childColumns = arrayOf("cityId"), onDelete = CASCADE)
    var cityId: Long = 0,
    val timezone: String?,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @Embedded val currently: DataPoint?,
    @Embedded val daily: DailyForecast?) {

  companion object {
    const val TABLE_NAME = "forecasts"
  }
}

data class DailyForecast(
    val summary: String?,
    val icon: String?,
    val data: List<DataPoint>?)

data class DataPoint(
    val time: Long = 0,
    val summary: String?,
    val icon: String?,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val temperature: Double)
