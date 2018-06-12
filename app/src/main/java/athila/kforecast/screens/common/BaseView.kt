package athila.kforecast.screens.common

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import athila.kforecast.R
import java.net.UnknownHostException

abstract class BaseView(context: Context) : CoordinatorLayout(context), BasicErrorsHandlerView {

  override fun handleBasicError(error: Throwable?): Boolean {
    val errorMessage = Snackbar.make(this, R.string.error_generic, Snackbar.LENGTH_LONG)
    if (error is UnknownHostException) {
      errorMessage.setText(R.string.error_no_internet_connection)
    }
    // TODO: add more generic error handling
    // default will show the most generic message
    errorMessage.show()
    return true
  }
}
