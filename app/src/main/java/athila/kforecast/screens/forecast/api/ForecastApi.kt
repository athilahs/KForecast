package athila.kforecast.screens.forecast.api

import android.arch.lifecycle.LiveData
import athila.kforecast.app.api.ApiConstants
import athila.kforecast.app.api.ApiResponse
import athila.kforecast.app.database.entity.Forecast
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastApi {

  @GET("forecast/" + ApiConstants.REST_API_KEY + "/{latitude},{longitude}")
  fun getForecast(@Path("latitude") latitude: Double,
      @Path("longitude") longitude: Double): LiveData<ApiResponse<Forecast>>
}