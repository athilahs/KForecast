package athila.kforecast.app.api.interceptors

import athila.kforecast.app.api.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicHeadersInterceptor : Interceptor {
  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
        .addHeader("Content-Type", ApiConstants.SUPPORTED_CONTENT_TYPE)
        // TODO: whatever you want to send in all requests
        .build()

    return chain.proceed(request)
  }
}