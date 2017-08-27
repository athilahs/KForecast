package athila.kforecast.app.common.usecase

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable

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

  private val mOnSubscribeStub: (Disposable) -> Unit = {}
  private val mOnNextStub: (T) -> Unit = {}
  private val mOnErrorStub: (Throwable) -> Unit = { throw RuntimeException("Not implemented", it) }
  private val mOnCompleteStub: () -> Unit = {}

  private var mSubscription: Disposable? = null
  /**
   * Builds an [Observable] which will be used when executing the current [BaseUseCase].
   * Any business rules should be placed here by manipulating the Observable built before returning it to the
   * calling client
   */
  protected abstract fun buildUseCaseObservable(params: Params?): Observable<T>

  /**
   * Executes the current use case using the provided Transformer.

   * @param useCaseSubscriber subscriber which will listen for results delivered by the Obsevable built
   * * with [.buildUseCaseObservable].
   * *
   * @param transformer the transformer to be applied on built observable. It can be to select execution / delivery thread
   */
  fun execute(params: Params? = null,
      onSubscribe: (Disposable) -> Unit = mOnSubscribeStub,
      onNext: (T) -> Unit = mOnNextStub,
      onError: (Throwable) -> Unit = mOnErrorStub,
      onComplete: () -> Unit = mOnCompleteStub,
      transformer: ObservableTransformer<T, T>? = null) {
    // Need to use the calling chain. It does not work if we break the chain like:
    // Observable o = buildUseCaseObservable();
    // if (transformer != null) {
    //      o.compose(transformer)
    // }
    if (transformer != null) {
      mSubscription = buildUseCaseObservable(params)
          .compose(transformer)
          .subscribe(onNext, onError, onComplete, onSubscribe)
    } else {
      mSubscription = buildUseCaseObservable(params)
          .subscribe(onNext, onError, onComplete, onSubscribe)
    }
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
