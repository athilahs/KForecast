package athila.kforecast.screens.forecast.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.di.ActivityScope
import athila.kforecast.app.di.modules.ViewModelModule
import athila.kforecast.screens.common.repository.CitiesRepository
import athila.kforecast.screens.forecast.api.ForecastApi
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.ForecastPresenter
import athila.kforecast.screens.forecast.core.ForecastView
import athila.kforecast.screens.forecast.core.ForecastViewModel
import athila.kforecast.screens.forecast.core.adapter.CitiesSpinnerAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapterPresenter
import athila.kforecast.screens.forecast.repository.DefaultForecastRepository
import athila.kforecast.screens.forecast.repository.ForecastRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class ForecastModule(private val activity: FragmentActivity) {

  @Provides
  @ActivityScope
  fun provideForecastView(forecastAdapter: ForecastAdapter,
      citiesSpinnerAdapter: CitiesSpinnerAdapter): ForecastContract.View = ForecastView(activity,
      forecastAdapter, citiesSpinnerAdapter)

  @Provides
  @ActivityScope
  fun provideForecastViewModel(factory: ViewModelProvider.Factory): ForecastContract.ViewModel {
    return ViewModelProviders.of(activity, factory).get(ForecastViewModel::class.java)
  }

  @Provides
  @ActivityScope
  fun provideForecastRepository(forecastApi: ForecastApi, forecastDao: ForecastDao): ForecastRepository =
      DefaultForecastRepository(forecastApi, forecastDao)

  @Provides
  @ActivityScope
  fun provideForecastPresenter(forecastView: ForecastContract.View,
      forecastViewModel: ForecastContract.ViewModel, testCitiesRepository: CitiesRepository): ForecastContract.Presenter {
    return ForecastPresenter(forecastView, forecastViewModel, testCitiesRepository)
  }

  @Provides
  @ActivityScope
  fun provideForecastAdapter(forecastAdapterPresenter: ForecastAdapterPresenter): ForecastAdapter = ForecastAdapter(
      forecastAdapterPresenter)

  @Provides
  @ActivityScope
  fun provideForecastListPresenter(): ForecastAdapterPresenter = ForecastAdapterPresenter()

  @Provides
  @ActivityScope
  fun provideCitiesSpinnerAdapter(): CitiesSpinnerAdapter = CitiesSpinnerAdapter(activity)
}
