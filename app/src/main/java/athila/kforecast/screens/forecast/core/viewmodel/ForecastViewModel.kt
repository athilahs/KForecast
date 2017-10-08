package athila.kforecast.screens.forecast.core.viewmodel

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Result
import athila.kforecast.app.common.rx.RxSchedulers
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.common.BaseViewModel
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase

class ForecastViewModel(private val getForecastUseCase: GetForecastUseCase) : BaseViewModel(), ForecastContract.ViewModel {

  override fun getForecast(params: GetForecastUseCase.GetForecastParams): LiveData<Result<Forecast>> {
    return getForecastUseCase.getAsLive(params, { s -> addSubscription(s) }, RxSchedulers.applyDefaultSchedulers())
  }
}