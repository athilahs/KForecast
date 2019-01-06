package athila.kforecast.screens.common.di

import athila.kforecast.app.database.dao.CityDao
import athila.kforecast.app.di.ActivityScope
import athila.kforecast.screens.common.repository.CitiesRepository
import athila.kforecast.screens.common.repository.DefaultCitiesRepository
import dagger.Module
import dagger.Provides

@Module
class CitiesRepositoryModule() {

  @Provides
  @ActivityScope
  fun provideCitiesRepository(citiesDao: CityDao): CitiesRepository = DefaultCitiesRepository(citiesDao)
}