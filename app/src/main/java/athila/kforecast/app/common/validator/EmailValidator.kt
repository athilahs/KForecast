package athila.kforecast.app.common.validator

import android.support.annotation.VisibleForTesting
import android.util.Patterns.EMAIL_ADDRESS
import java.util.regex.Pattern

class EmailValidator(private var mValidationPattern: Pattern = EMAIL_ADDRESS) : Validator<String> {

  override fun isValid(target: String): Boolean {
    return mValidationPattern.matcher(target).matches()
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun setValidationPattern(validationPattern: Pattern) {
    mValidationPattern = validationPattern
  }
}
