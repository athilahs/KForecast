package athila.kforecast.screens.common.usecase

import athila.kforecast.app.common.usecase.ExecutableUseCase
import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.database.entity.City
import athila.kforecast.screens.common.usecase.InsertCityUseCase.InsertCityParam
import io.reactivex.Flowable

class InsertCityUseCase(private val cityDao: CityDao) : ExecutableUseCase<Unit, InsertCityParam>() {

  override fun buildUseCaseObservable(params: InsertCityParam?): Flowable<Unit> {
    return if (params == null) {
      Flowable.error(IllegalArgumentException("Please, supply the city to be inserted"))
    } else {
      Flowable.fromCallable({ cityDao.insertCity(params.city) })
    }
  }

  class InsertCityParam(val city: City)
}