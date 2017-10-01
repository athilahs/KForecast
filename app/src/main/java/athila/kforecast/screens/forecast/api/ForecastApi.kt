package athila.kforecast.screens.forecast.api

import athila.kforecast.app.api.ApiConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastApi {

  @GET("forecast/" + ApiConstants.REST_API_KEY + "/{latitude},{longitude}")
  fun getForecast(@Path("latitude") latitude: Double,
      @Path("longitude") longitude: Double): Observable<Forecast>
}