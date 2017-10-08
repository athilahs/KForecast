package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Result
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.common.BaseAdapterPresenter
import athila.kforecast.screens.common.BasePresenterContract
import athila.kforecast.screens.common.BaseViewContract
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase
import io.reactivex.Observable


interface ForecastContract {
  interface View : BaseViewContract {
    fun showEmptyView()
    fun observeCityChanges(): Observable<String>
    fun setForecast(forecast: Forecast)
    fun showProgress()
    fun hideProgress()
    fun observeRefreshButton(): Observable<Any>
  }

  interface Presenter : BasePresenterContract {
    // nothing specific
  }

  interface AdapterPresenter : BaseAdapterPresenter<Forecast, ItemView>

  interface ItemView {
    fun dayOfWeek(dayOfWeek: String)
    fun weatherIcon(icon: Int)
    fun summary(summary: String)
    fun maxTemperature(maxTemperature: Double)
    fun minTemperature(minTemperature: Double)
  }

  interface ViewModel {
    fun getForecast(params: GetForecastUseCase.GetForecastParams): LiveData<Result<Forecast>>
  }
}