package athila.kforecast.app.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import android.arch.lifecycle.ViewModelStores
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log

class ViewModelParameterizedProvider {

  companion object {


    lateinit var provider: ViewModelProvider

    private var viewModelStore: ViewModelStore? = null

    fun of(fragment: Fragment): Companion {
      viewModelStore = ViewModelStores.of(fragment)
      return this@Companion
    }

    fun of(fragment: android.app.Fragment): Companion {
      val fragAct: FragmentActivity = fragment.activity as FragmentActivity;
      return of(fragAct)
    }

    fun of(activity: FragmentActivity): Companion {
      viewModelStore = ViewModelStores.of(activity)
      return this@Companion
    }


    fun with(constructorParams: Array<out Any>) = ViewModelProvider(viewModelStore, parametrizedFactory(constructorParams))

    private fun parametrizedFactory(constructorParams: Array<out Any>) = ParametrizedFactory(constructorParams)
  }

  class ParametrizedFactory(private var constructorParams: Array<out Any>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
      if (modelClass == null) {
        throw IllegalArgumentException("Target ViewModel class can not be null")
      }
      Log.w("ParametrizedFactory", "Don't use callbacks or Context as parameters in order to avoid leaks!!")
      return when (constructorParams.size) {
        0 -> {
          modelClass.newInstance()
        }
        else -> {
          val parameterClasses: Array<Class<*>> = constructorParams.map { param -> param.javaClass }.toList().toTypedArray()
          modelClass.getConstructor(*parameterClasses).newInstance(*constructorParams)
        }
      }
    }
  }
}