package athila.kforecast.app.common.usecase

import io.reactivex.FlowableTransformer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 *
 * By convention each ExecutableUseCase implementation will return the result using a [rx.Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class ExecutableUseCase<T, in Params> : BaseUseCase<T, Params> {

  private var mSubscription: Disposable? = null

  /**
   * Executes the current use case using the provided Transformer.

   * @param useCaseSubscriber subscriber which will listen for results delivered by the Observable built
   * * with [.buildUseCaseObservable].
   * *
   * @param transformer the transformer to be applied on built observable. It can be to select execution / delivery thread
   */
  fun execute(params: Params? = null,
      onSubscribe: (Subscription) -> Unit = {},
      onNext: (T) -> Unit = {},
      onError: (Throwable) -> Unit = { throw RuntimeException("Not implemented", it) },
      onComplete: () -> Unit = {},
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
