package athila.kforecast.app.di.modules

import athila.kforecast.BuildConfig
import athila.kforecast.app.api.interceptors.BasicHeadersInterceptor
import athila.kforecast.app.di.FirebaseRetrofit
import athila.kforecast.app.di.ForecastRetrofit
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

  companion object {
    // TODO: change it
    @JvmStatic
    val FIREBASE_BASE_URL = "https://firebase.com"

    @JvmStatic
    val FORECAST_BASE_URL = "https://api.forecast.io/"
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
        .addInterceptor(BasicHeadersInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
          level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
            HttpLoggingInterceptor.Level.NONE
        })

    return builder.build()
  }

  @Provides
  @Singleton
  fun provideBaseRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
  }

  @Provides
  @Singleton
  @FirebaseRetrofit
  fun provideFirebaseRetrofit(baseRetrofitBuilder: Retrofit.Builder): Retrofit = baseRetrofitBuilder.baseUrl(
      FIREBASE_BASE_URL).build()

  @Provides
  @Singleton
  @ForecastRetrofit
  fun provideForecastRetrofit(baseRetrofitBuilder: Retrofit.Builder): Retrofit = baseRetrofitBuilder.baseUrl(
      FORECAST_BASE_URL).build()
}