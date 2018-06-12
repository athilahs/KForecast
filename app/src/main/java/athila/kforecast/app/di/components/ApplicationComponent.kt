package athila.kforecast.app.di.components

import android.app.Application
import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.di.modules.ApplicationModule
import athila.kforecast.app.di.modules.DatabaseModule
import athila.kforecast.app.di.modules.NetworkModule
import athila.kforecast.app.di.modules.RetrofitModule
import athila.kforecast.screens.forecast.api.ForecastApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class, NetworkModule::class, RetrofitModule::class,
    DatabaseModule::class))
interface ApplicationComponent {

  // expose to sub graphs
  fun application(): Application

  // APIs
  fun forecastApi(): ForecastApi

  // DAOs
  fun forecastDao(): ForecastDao

  fun cityDao(): CityDao
}