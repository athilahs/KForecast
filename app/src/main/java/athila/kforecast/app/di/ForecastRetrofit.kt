package athila.kforecast.app.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Qualifier
@Target(FUNCTION, VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class ForecastRetrofit