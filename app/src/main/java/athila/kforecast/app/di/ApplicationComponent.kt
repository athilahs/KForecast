package athila.kforecast.app.di

import athila.kforecast.app.KForecastApplication
import athila.kforecast.forecast.api.ForecastApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class, RetrofitModule::class))
interface ApplicationComponent {

  // expose to sub graphs
  fun application(): KForecastApplication

  fun forecastApi(): ForecastApi
}