package athila.kforecast.screens.common.widget

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import athila.kforecast.R
import athila.kforecast.app.common.validator.ValidationException

/**
 * Created by athila on 10/03/16.
 */
class ShakerEditText : ValidatableEditText {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    val a = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ValidatableEditText,
        0, 0)

    try {
      mErrorMessage = a.getString(R.styleable.ValidatableEditText_errorMessage)
    } finally {
      a.recycle()
    }
  }

  fun setErrorMessage(@StringRes errorMessageId: Int) {
    mErrorMessage = mContext.getString(errorMessageId)
  }

  @Throws(ValidationException::class)
  override fun validate() {
    if (!isInputValid) {
      val shake = AnimationUtils.loadAnimation(mContext, R.anim.shake)
      startAnimation(shake)
      error = mErrorMessage

      // throws exception so the execution can be interrupted when validation fails
      throw ValidationException()
    }
  }
}
