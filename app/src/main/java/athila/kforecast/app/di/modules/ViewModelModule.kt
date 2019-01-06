package athila.kforecast.app.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import athila.kforecast.app.di.KForecastViewModelFactory
import athila.kforecast.app.di.ViewModelKey
import athila.kforecast.screens.forecast.core.ForecastViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
  @Binds
  fun bindViewModelFactory(factory: KForecastViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(ForecastViewModel::class)
  fun bindForecastViewModel(forecastViewModel: ForecastViewModel): ViewModel
}