package athila.kforecast.app.common.usecase

import android.arch.lifecycle.LiveData
import athila.kforecast.app.extensions.toLiveData
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 *
 * By convention each BaseUseCase implementation will return the result using a [rx.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class BaseUseCase<T, in Params> {

  private val mOnSubscribeStub: (Subscription) -> Unit = {}
  private val mOnNextStub: (T) -> Unit = {}
  private val mOnErrorStub: (Throwable) -> Unit = { throw RuntimeException("Not implemented", it) }
  private val mOnCompleteStub: () -> Unit = {}

  private var mSubscription: Disposable? = null
  /**
   * Builds an [Observable] which will be used when executing the current [BaseUseCase].
   * Any business rules should be placed here by manipulating the Observable built before returning it to the
   * calling client
   */
  protected abstract fun buildUseCaseObservable(params: Params?): Flowable<T>

  /**
   * Executes the current use case using the provided Transformer.

   * @param useCaseSubscriber subscriber which will listen for results delivered by the Observable built
   * * with [.buildUseCaseObservable].
   * *
   * @param transformer the transformer to be applied on built observable. It can be to select execution / delivery thread
   */
  fun execute(params: Params? = null,
      onSubscribe: (Subscription) -> Unit = mOnSubscribeStub,
      onNext: (T) -> Unit = mOnNextStub,
      onError: (Throwable) -> Unit = mOnErrorStub,
      onComplete: () -> Unit = mOnCompleteStub,
      transformer: FlowableTransformer<T, T>? = null) {
    // Need to use the calling chain. It does not work if we break the chain like:
    // Observable o = buildUseCaseObservable();
    // if (transformer != null) {
    //      o.compose(transformer)
    // }
    mSubscription = if (transformer != null) {
      buildUseCaseObservable(params)
          .compose(transformer)
          .subscribe(onNext, onError, onComplete, onSubscribe)
    } else {
      buildUseCaseObservable(params)
          .subscribe(onNext, onError, onComplete, onSubscribe)
    }
  }

  // TODO: how to deal with subscriptions and live data? should we only deal with live data? should we return the flowable to
  // the caller and rely on it to dispose it? In this case, the ViewModels will have to deal with both types of data
  // (subscriptions and live data.
  fun executeLive(params: Params? = null, transformer: FlowableTransformer<T, T>? = null): LiveData<T> {
    val flowable: Flowable<T> = if (transformer != null) {
      buildUseCaseObservable(params)
          .compose(transformer)
    } else {
      buildUseCaseObservable(params)
    }
    return flowable.toLiveData()
  }

  /**
   * Unsubscribes from current [rx.Subscription].
   */
  fun dispose() {
    mSubscription?.let {
      if (!it.isDisposed) {
        it.dispose()
      }
    }
  }
}
