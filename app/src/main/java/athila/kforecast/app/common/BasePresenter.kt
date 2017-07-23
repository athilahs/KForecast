package athila.kforecast.app.common

import android.support.annotation.VisibleForTesting

/**
 * Created by athila on 14/03/16.
 */
abstract class BasePresenter {

  @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
  fun handleBasicError(view: BaseViewContract, error: Throwable): Boolean {
    return view is BasicErrorsHandlerView && view.handleBasicError(error)
  }
}
