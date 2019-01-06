package athila.kforecast.app.database.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = Forecast.TABLE_NAME)
@TypeConverters(DataPointsConverter::class)
data class Forecast @Ignore constructor(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ForeignKey(entity = City::class, parentColumns = ["id"], childColumns = ["cityId"], onDelete = CASCADE)
    var cityId: Long,
    var timezone: String?,
    var latitude: Double,
    var longitude: Double,
    @Embedded(prefix = "currently_") var currently: DataPoint?,
    @Embedded(prefix = "daily_") var daily: DailyForecast?) {

  constructor() : this(id = 0, cityId = 0, timezone = "", latitude = 0.0, longitude = 0.0, currently = null, daily = null)

  companion object {
    const val TABLE_NAME = "forecasts"
  }
}

data class DailyForecast @Ignore constructor(
    var summary: String? = "",
    var icon: String? = "",
    var data: List<DataPoint>? = null) {

  constructor() : this(summary = "", icon = "", data = null)
}

data class DataPoint @Ignore constructor(
    var time: Long = 0,
    var summary: String? = "",
    var icon: String? = null,
    var temperatureMin: Double = 0.0,
    var temperatureMax: Double = 0.0,
    var temperature: Double = 0.0) {

  constructor() : this(time = 0, summary = "", icon = null, temperatureMin = 0.0, temperatureMax = 0.0, temperature = 0.0)
}

class DataPointsConverter {
  @TypeConverter
  fun jsonToDataPoints(dataPointsJson: String): List<DataPoint> {
    val listType = object : TypeToken<List<DataPoint>>() {}.type

    return Gson().fromJson<List<DataPoint>>(dataPointsJson, listType)
  }

  @TypeConverter
  fun dataPointsToJson(dataPoints: List<DataPoint>): String {
    val listType = object : TypeToken<List<DataPoint>>() {}.type
    return Gson().toJson(dataPoints, listType)
  }
}