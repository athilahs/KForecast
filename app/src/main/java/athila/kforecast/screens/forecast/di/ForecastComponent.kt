package athila.kforecast.screens.forecast.di

import athila.kforecast.app.di.FragmentScope
import athila.kforecast.app.di.components.ApplicationComponent
import athila.kforecast.screens.common.di.CitiesRepositoryModule
import athila.kforecast.screens.forecast.core.ForecastFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [ForecastModule::class, CitiesRepositoryModule::class])
interface ForecastComponent {

  fun inject(forecastFragment: ForecastFragment)
}