package athila.kforecast.app.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Lets us use [Inject] annotations on [ViewModel] classes.
 *
 * To use it, include the [ViewModelModule] in the module that binds the ViewModel itself.
 */
class KForecastViewModelFactory @Inject constructor(
    private val providers: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {

    // check if modelClass is provided or is a superclass of provided one
    val provider = providers[modelClass]
        ?: providers.entries.find { modelClass.isAssignableFrom(it.key) }?.value
        ?: throw IllegalArgumentException("Unknown model class $modelClass")

    try {
      @Suppress("UNCHECKED_CAST")
      return provider.get() as T
    } catch (e: Exception) {
      throw RuntimeException("There was an error providing an instance of $modelClass", e)
    }
  }
}