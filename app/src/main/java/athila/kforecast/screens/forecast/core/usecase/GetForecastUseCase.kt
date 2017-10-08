package athila.kforecast.screens.forecast.core.usecase

import athila.kforecast.app.common.usecase.LiveUseCase
import athila.kforecast.app.database.dao.ForecastDao
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.forecast.core.usecase.GetForecastUseCase.GetForecastParams
import io.reactivex.Flowable

class GetForecastUseCase(private val forecastDao: ForecastDao) : LiveUseCase<Forecast, GetForecastParams>() {


  override fun buildUseCaseObservable(params: GetForecastParams?): Flowable<Forecast> {
    return if (params == null) {
      Flowable.error(IllegalArgumentException("Please, supply the city to get a forecast"))
    } else {
      forecastDao.forecastForCity(params.city.uid)
    }
  }

  class GetForecastParams(val city: City)

}
