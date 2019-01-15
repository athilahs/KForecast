package athila.kforecast.screens.forecast.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.di.FragmentScope
import athila.kforecast.app.di.modules.ViewModelModule
import athila.kforecast.screens.common.repository.CitiesRepository
import athila.kforecast.screens.forecast.api.ForecastApi
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.ForecastFragment
import athila.kforecast.screens.forecast.core.ForecastPresenter
import athila.kforecast.screens.forecast.core.ForecastViewModel
import athila.kforecast.screens.forecast.core.adapter.CitiesSpinnerAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapterPresenter
import athila.kforecast.screens.forecast.repository.DefaultForecastRepository
import athila.kforecast.screens.forecast.repository.ForecastRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class ForecastModule(private val fragment: ForecastFragment) {

  @Provides
  @FragmentScope
  fun provideForecastView(): ForecastContract.View = fragment

  @Provides
  @FragmentScope
  fun provideForecastViewModel(factory: ViewModelProvider.Factory): ForecastContract.ViewModel {
    return ViewModelProviders.of(fragment, factory).get(ForecastViewModel::class.java)
  }

  @Provides
  @FragmentScope
  fun provideForecastRepository(forecastApi: ForecastApi, forecastDao: ForecastDao): ForecastRepository =
      DefaultForecastRepository(forecastApi, forecastDao)

  @Provides
  @FragmentScope
  fun provideForecastPresenter(forecastView: ForecastContract.View,
      forecastViewModel: ForecastContract.ViewModel, testCitiesRepository: CitiesRepository): ForecastContract.Presenter {
    return ForecastPresenter(forecastView, forecastViewModel, testCitiesRepository)
  }

  @Provides
  @FragmentScope
  fun provideForecastAdapter(forecastAdapterPresenter: ForecastAdapterPresenter): ForecastAdapter = ForecastAdapter(
      forecastAdapterPresenter)

  @Provides
  @FragmentScope
  fun provideForecastAdapterPresenter(): ForecastAdapterPresenter = ForecastAdapterPresenter()

  @Provides
  @FragmentScope
  fun provideCitiesSpinnerAdapter(): CitiesSpinnerAdapter = CitiesSpinnerAdapter(fragment.context!!)
}
