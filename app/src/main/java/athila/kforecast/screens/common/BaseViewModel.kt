package athila.kforecast.screens.common

import android.arch.lifecycle.ViewModel
import athila.kforecast.app.common.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
  private val parentJob: Job = Job()

  // 'launch' coroutines created by the subclasses will use the coroutineContext from this class which is already appending the
  // parent job and, therefore, does not need to be specified


  protected fun <T> asyncAndAttachToParent(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return async(context = AppExecutors.bgThread) { block() }
  }

  override val coroutineContext: CoroutineContext
    get() = AppExecutors.mainThread + parentJob

  override fun onCleared() {
    parentJob.cancel()
    super.onCleared()
  }
}