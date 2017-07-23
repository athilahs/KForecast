package athila.kforecast.forecast.di

import android.content.Context
import athila.kforecast.forecast.core.ForecastContract
import athila.kforecast.forecast.core.ForecastView
import dagger.Module
import dagger.Provides

@Module
class ForecastModule(val context: Context) {

  @Provides
  @ForecastScope
  fun provideForecastView(): ForecastContract.View = ForecastView(context)
}
