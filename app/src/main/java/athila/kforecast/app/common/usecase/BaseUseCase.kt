package athila.kforecast.app.common.usecase

import io.reactivex.Flowable

interface BaseUseCase<T, in Params> {

  /**
   * Builds an [Observable] which will be used when executing the current [BaseUseCase].
   * Any business rules should be placed here by manipulating the Observable built before returning it to the
   * calling client
   */
  fun buildUseCaseObservable(params: Params?): Flowable<T>
}