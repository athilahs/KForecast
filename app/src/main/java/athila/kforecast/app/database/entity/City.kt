package athila.kforecast.app.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double) {

  companion object {
    const val TABLE_NAME = "cities"
  }
}

