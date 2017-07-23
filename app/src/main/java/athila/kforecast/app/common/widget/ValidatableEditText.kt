package athila.kforecast.app.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import athila.kforecast.app.common.validator.ValidationException
import athila.kforecast.app.common.validator.Validator

open class ValidatableEditText : EditText {

  var mValidator: Validator<String>? = null
  var mErrorMessage: String? = null
  protected var mContext: Context

  protected constructor(context: Context) : super(context) {
    mContext = context
  }

  protected constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    mContext = context
  }

  protected val isInputValid: Boolean
    get() = mValidator?.isValid(text.toString()) ?: false

  @Throws(ValidationException::class)
  open fun validate() {
    if (!isInputValid) {
      throw ValidationException()
    }
  }
}
