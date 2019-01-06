package athila.kforecast.app.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = City.TABLE_NAME)
data class City @Ignore constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var latitude: Double,
    var longitude: Double) {

  // Need this for room
  constructor() : this(id = 0, name = "", latitude = 0.0, longitude = 0.0)

  companion object {
    const val TABLE_NAME = "cities"
  }
}