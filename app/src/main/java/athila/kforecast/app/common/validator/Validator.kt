package athila.kforecast.app.common.validator

/**
 * Created by athila on 10/07/15.
 */
interface Validator<in T> {
  fun isValid(target: T): Boolean
}
