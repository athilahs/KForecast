package athila.kforecast.screens.forecast.di

import athila.kforecast.app.di.ActivityScope
import athila.kforecast.app.di.components.ApplicationComponent
import athila.kforecast.screens.forecast.ForecastActivity
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(
    ForecastModule::class))
interface ForecastComponent {

  fun inject(forecastActivity: ForecastActivity)
}