package athila.kforecast.screens.forecast.di

import android.support.v4.app.FragmentActivity
import athila.kforecast.app.common.ViewModelParameterizedProvider
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.di.ActivityScope
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.ForecastPresenter
import athila.kforecast.screens.forecast.core.ForecastView
import athila.kforecast.screens.forecast.core.ForecastViewModel
import athila.kforecast.screens.forecast.core.adapter.CitiesSpinnerAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapter
import athila.kforecast.screens.forecast.core.adapter.ForecastAdapterPresenter
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase
import dagger.Module
import dagger.Provides

@Module
class ForecastModule(private val activity: FragmentActivity) {

  @Provides
  @ActivityScope
  fun provideForecastView(forecastAdapter: ForecastAdapter,
      citiesSpinnerAdapter: CitiesSpinnerAdapter): ForecastContract.View = ForecastView(activity,
      forecastAdapter, citiesSpinnerAdapter)

  @Provides
  @ActivityScope
  fun provideForecastViewModel(getForecastUseCase: GetForecastUseCase): ForecastContract.ViewModel {
    return ViewModelParameterizedProvider
        .of(activity)
        .with(arrayOf(getForecastUseCase))
        .get(ForecastViewModel::class.java)
  }

  @Provides
  @ActivityScope
  fun provideGetForecastUsecase(forecastDao: ForecastDao): GetForecastUseCase = GetForecastUseCase(forecastDao)

  @Provides
  @ActivityScope
  fun provideForecastPresenter(forecastView: ForecastContract.View,
      forecastViewModel: ForecastContract.ViewModel): ForecastContract.Presenter {
    return ForecastPresenter(forecastView, forecastViewModel)
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
