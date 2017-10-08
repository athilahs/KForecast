package athila.kforecast.app.common.usecase

import android.arch.lifecycle.LiveData
import athila.kforecast.app.common.Result
import athila.kforecast.app.extensions.toLiveData
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.reactivestreams.Subscription

abstract class LiveUseCase<T, in Params> : BaseUseCase<T, Params> {

  fun getAsLive(params: Params? = null,
      onSubscribe: (Subscription) -> Unit = {},
      transformer: FlowableTransformer<Result<T>, Result<T>>? = null): LiveData<Result<T>> {

    val flowable: Flowable<Result<T>> = if (transformer != null) {
      buildUseCaseObservable(params)
          .doOnSubscribe(onSubscribe)
          .map { Result.success(it) }
          .onErrorReturn { error: Throwable -> Result.error(throwable = error) }
          .compose(transformer)
    } else {
      buildUseCaseObservable(params)
          .doOnSubscribe(onSubscribe)
          .map { Result.success(it) }
          .onErrorReturn { error: Throwable -> Result.error(throwable = error) }
    }
    return flowable.toLiveData()
  }
}