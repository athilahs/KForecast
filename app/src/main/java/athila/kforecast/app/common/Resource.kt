package athila.kforecast.app.common

import athila.kforecast.app.common.Status.ERROR
import athila.kforecast.app.common.Status.IDLE
import athila.kforecast.app.common.Status.LOADING
import athila.kforecast.app.common.Status.SUCCESS
import java.util.Optional


class Resource<out T> private constructor(val status: Status, val data: T?, val error: Throwable?) {

  companion object {
    fun <T> success(data: T?): Resource<T> {
      return Resource(SUCCESS, data, null)
    }

    fun <T> success(): Resource<T> {
      return Resource(SUCCESS, null, null)
    }

    fun <T> idle(data: T?): Resource<T> {
      return Resource(IDLE, data, null)
    }

    fun <T> idle(): Resource<T> {
      return Resource(IDLE, null, null)
    }

    fun <T> error(error: Throwable): Resource<T> {
      return Resource(ERROR, null, error)
    }

    fun <T> loading(data: T?): Resource<T> {
      return Resource(LOADING, data, null)
    }

    fun <T> loading(): Resource<T> {
      return Resource(LOADING, null, null)
    }
  }

  fun isSuccessfull(): Boolean = error != null
}

enum class Status {
  SUCCESS,
  LOADING,
  ERROR,
  IDLE
}