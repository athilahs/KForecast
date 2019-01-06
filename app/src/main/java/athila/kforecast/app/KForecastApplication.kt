package athila.kforecast.app

import android.app.Application
import athila.kforecast.app.di.components.ApplicationComponent
import athila.kforecast.app.di.components.DaggerApplicationComponent
import athila.kforecast.app.di.modules.ApplicationModule
import com.facebook.stetho.Stetho

class KForecastApplication : Application() {

  private lateinit var applicationComponent: ApplicationComponent

  override fun onCreate() {
    super.onCreate()

    initializeInjector()
    Stetho.initializeWithDefaults(this);
  }

  private fun initializeInjector() {
    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  fun getApplicationComponent(): ApplicationComponent = applicationComponent
}