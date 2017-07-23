package athila.kforecast.forecast.di

import athila.kforecast.app.di.ApplicationComponent
import athila.kforecast.forecast.ForecastActivity
import dagger.Component

@ForecastScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ForecastModule::class))
interface ForecastComponent {

  fun inject(forecastActivity: ForecastActivity)
}