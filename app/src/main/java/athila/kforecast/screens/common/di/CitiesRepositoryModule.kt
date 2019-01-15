package athila.kforecast.screens.common.di

import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.di.FragmentScope
import athila.kforecast.screens.common.repository.CitiesRepository
import athila.kforecast.screens.common.repository.DefaultCitiesRepository
import dagger.Module
import dagger.Provides

@Module
class CitiesRepositoryModule() {

  @Provides
  @FragmentScope
  fun provideCitiesRepository(citiesDao: CityDao): CitiesRepository = DefaultCitiesRepository(citiesDao)
}