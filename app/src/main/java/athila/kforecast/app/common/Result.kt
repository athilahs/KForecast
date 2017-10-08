package athila.kforecast.app.common


class Result<out T> private constructor(
    val data: T?, val error: Throwable?) {
  companion object {

    fun <T> success(data: T): Result<T> {
      return Result(data, null)
    }

    fun <T> error(data: T? = null, throwable: Throwable): Result<T> {
      return Result(data, throwable)
    }
  }

  fun isSuccess() = error == null
}