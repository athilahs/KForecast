package athila.kforecast.app.common

/**
 * Created by athila on 17/03/16.
 */
interface BasicErrorsHandlerView {
  fun handleBasicError(error: Throwable): Boolean
}
