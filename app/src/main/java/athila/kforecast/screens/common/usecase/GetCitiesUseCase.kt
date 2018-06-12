package athila.kforecast.screens.common.usecase

import athila.kforecast.app.common.usecase.LiveUseCase
import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.database.entity.City
import io.reactivex.Flowable

class GetCitiesUseCase(private val cityDao: CityDao) : LiveUseCase<List<City>, Unit>() {

  override fun buildUseCaseObservable(params: Unit?): Flowable<List<City>> {
    return cityDao.getAllCities()
  }
}