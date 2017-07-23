package athila.kforecast.app

import android.app.Application
import athila.kforecast.app.di.ApplicationComponent
import athila.kforecast.app.di.ApplicationModule
import athila.kforecast.app.di.DaggerApplicationComponent

class KForecastApplication : Application() {

  private lateinit var applicationComponent: ApplicationComponent

  override fun onCreate() {
    super.onCreate()

    initializeInjector()
  }

  private fun initializeInjector() {
    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  fun getApplicationComponent(): ApplicationComponent = applicationComponent
}