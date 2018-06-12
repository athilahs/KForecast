package athila.kforecast.app.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = City.TABLE_NAME)
data class City(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    var name: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0) {

  companion object {
    const val TABLE_NAME = "cities"
  }
}

