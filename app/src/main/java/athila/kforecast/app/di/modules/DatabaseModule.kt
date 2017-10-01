package athila.kforecast.app.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import athila.kforecast.app.database.KForecastDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(application: Application): KForecastDatabase {
    return Room.databaseBuilder(application, KForecastDatabase::class.java, "kforecast.db").build()
  }

  @Provides
  @Singleton
  fun provideForecastDao(kForecastDatabase: KForecastDatabase) = kForecastDatabase.forecastDao()

  @Provides
  @Singleton
  fun provideCityDao(kForecastDatabase: KForecastDatabase) = kForecastDatabase.cityDao()
}