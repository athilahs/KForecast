package athila.kforecast.app.database.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = Forecast.TABLE_NAME)
@TypeConverters(DataPointsConverter::class)
data class Forecast(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    @ForeignKey(entity = City::class, parentColumns = arrayOf("uid"), childColumns = arrayOf("cityId"), onDelete = CASCADE)
    var cityId: Long = 0,
    var timezone: String? = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    @Embedded(prefix = "currently_") var currently: DataPoint? = null,
    @Embedded(prefix = "daily_") var daily: DailyForecast? = null) {

  companion object {
    const val TABLE_NAME = "forecasts"
  }
}

data class DailyForecast(
    var summary: String? = "",
    var icon: String? = "",
    var data: List<DataPoint>? = null
//    var data: DataPoints? = null
)

data class DataPoint(
    var time: Long = 0,
    var summary: String? = "",
    var icon: String? = null,
    var temperatureMin: Double = 0.0,
    var temperatureMax: Double = 0.0,
    var temperature: Double = 0.0)

//data class DataPoints(var data: List<DataPoint>)

class DataPointsConverter {
  @TypeConverter
  fun jsonToDataPoints(dataPointsJson: String): List<DataPoint> {
    val listType = object : TypeToken<List<DataPoint>>() {}.type

    return Gson().fromJson<List<DataPoint>>(dataPointsJson,listType)
  }

  @TypeConverter
  fun dataPointsToJson(dataPoints: List<DataPoint>): String {
    val listType = object : TypeToken<List<DataPoint>>() {}.type
    return Gson().toJson(dataPoints, listType)
  }
}