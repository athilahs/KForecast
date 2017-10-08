package athila.kforecast.app.common.rx

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by athila on 15/03/16.
 */
object RxSchedulers {
  /**
   * Execute the operation on a new thread (from thread pool) and listen on the main thread.
   * It can be used for I/O operations

   * @return the transformer properly configured
   */
  fun <T> applyDefaultSchedulers(): FlowableTransformer<T, T> {
    return FlowableTransformer {
      it.subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }

  /**
   * Execute and listen the operation on the current thread. It can be used for scenarios where the
   * parallelism is already provided (operations executed by IntentService, for example)

   * @return the transformer properly configured
   */
  fun <T> applyImmediateSchedulers(): FlowableTransformer<T, T> {
    return FlowableTransformer {
      it.subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
    }
  }
}
