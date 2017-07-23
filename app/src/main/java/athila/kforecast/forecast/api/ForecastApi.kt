package athila.kforecast.forecast.api

import athila.kforecast.BuildConfig
import athila.kforecast.app.api.ApiConstants
import athila.kforecast.forecast.model.Forecast
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastApi {

  @GET("forecast/" + ApiConstants.REST_API_KEY + "/{latitude},{longitude}")
  fun getForecast(@Path("latitude") latitude: Double,
      @Path("longitude") longitude: Double): Observable<Forecast>
}