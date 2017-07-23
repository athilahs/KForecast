package athila.kforecast.app.di

import athila.kforecast.app.KForecastApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: KForecastApplication) {

  @Provides
  @Singleton
  fun provideApplication(): KForecastApplication = application
}