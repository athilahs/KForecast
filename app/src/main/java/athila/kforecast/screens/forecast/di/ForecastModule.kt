package athila.kforecast.screens.forecast.di

import android.content.Context
import athila.kforecast.app.di.ActivityScope
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.ForecastView
import dagger.Module
import dagger.Provides

@Module
class ForecastModule(val context: Context) {

  @Provides
  @ActivityScope
  fun provideForecastView(): ForecastContract.View = ForecastView(
      context)
}
