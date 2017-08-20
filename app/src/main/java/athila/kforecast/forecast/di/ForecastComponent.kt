package athila.kforecast.forecast.di

import athila.kforecast.app.di.ActivityScope
import athila.kforecast.app.di.ApplicationComponent
import athila.kforecast.forecast.ForecastActivity
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ForecastModule::class,
    ForecastListModule::class))
interface ForecastComponent {

  fun inject(forecastActivity: ForecastActivity)
}