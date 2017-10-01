package athila.kforecast.screens.forecast.di

import android.content.Context
import athila.kforecast.app.di.ActivityScope
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastListPresenter
import dagger.Module
import dagger.Provides

@Module
class ForecastListModule(val context: Context) {

  @Provides
  @ActivityScope
  fun providesForecastAdapter(forecastListPresenter: ForecastListPresenter): ForecastAdapter = ForecastAdapter(
      forecastListPresenter)

  @Provides
  @ActivityScope
  fun providesForecastListPresenter(): ForecastListPresenter = ForecastListPresenter()
}