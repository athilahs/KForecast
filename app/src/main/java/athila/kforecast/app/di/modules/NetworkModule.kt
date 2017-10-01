package athila.kforecast.app.di.modules

import athila.kforecast.app.di.ForecastRetrofit
import athila.kforecast.screens.forecast.api.ForecastApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

  /** Since we are going to have more than one base URL, would it be better to put all the Retrofit instances (one for
   * each base URL) in this module (probably holding Retrofit objects in memory unnecessarily or keep them in their specific
   * modules (may have impact in the performance)
   **/
  @Provides
  @Singleton
  fun provideForecastApi(@ForecastRetrofit forecastRetrofit: Retrofit): ForecastApi = forecastRetrofit.create<ForecastApi>(
      ForecastApi::class.java)
}