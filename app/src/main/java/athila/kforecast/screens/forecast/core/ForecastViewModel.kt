package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import athila.kforecast.app.common.Result
import athila.kforecast.app.common.rx.RxSchedulers
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.common.BaseViewModel
import athila.kforecast.screens.common.usecase.GetCitiesUseCase
import athila.kforecast.screens.common.usecase.InsertCityUseCase
import athila.kforecast.screens.common.usecase.InsertCityUseCase.InsertCityParam
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase.GetForecastParams
import java.util.Random

class ForecastViewModel(private val getForecastUseCase: GetForecastUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val insertCityUseCase: InsertCityUseCase) : BaseViewModel(), ForecastContract.ViewModel {

  private var forecastLiveData: MutableLiveData<Result<Forecast>>? = null

  override fun getCurrentForecast(params: GetForecastUseCase.GetForecastParams): LiveData<Result<Forecast>> {
    if (forecastLiveData == null) {
      forecastLiveData = MutableLiveData()
      executeGetForecast(params)
    }

    return forecastLiveData as LiveData<Result<Forecast>>
  }

  override fun switchCity(params: GetForecastParams) {
    if (forecastLiveData != null) {
      getForecastUseCase.dispose()
      executeGetForecast(params)
    }
  }

  override fun getCities(): LiveData<Result<List<City>>> {
    return getCitiesUseCase.getAsLive(onSubscribe = { s -> addSubscription(s) },
        transformer = RxSchedulers.applyDefaultSchedulers())
  }

  // Test
  override fun insertRandomCity() {
    val r = Random()
    val minLongitude = -8.932439
    val maxLongitude = -6.060655
    val minLatitude = 51.646024
    val maxLatitude = 55.115816

    val randomLatitude = minLatitude + (maxLatitude - minLatitude) * r.nextDouble()
    val randomLongitude = minLongitude + (maxLongitude - minLongitude) * r.nextDouble()

    val randomCity = City(name = "Rand ($randomLatitude, $randomLongitude)", latitude = randomLatitude,
        longitude = randomLongitude)

    insertCityUseCase.execute(InsertCityParam(randomCity), transformer = RxSchedulers.applyDefaultSchedulers())
  }

  private fun executeGetForecast(params: GetForecastParams) {
    getForecastUseCase.execute(params = params,
        onNext = { forecast ->
          forecastLiveData?.value = Result.success(forecast)
        },
        onError = { throwable ->
          forecastLiveData?.value = Result.error(throwable = throwable)
        },
        transformer = RxSchedulers.applyDefaultSchedulers())
  }

}
