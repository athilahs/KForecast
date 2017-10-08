package athila.kforecast.screens.common

import android.arch.lifecycle.ViewModel
import org.reactivestreams.Subscription

open abstract class BaseViewModel : ViewModel() {
  private val subscriptions: MutableList<Subscription> = mutableListOf()

  fun addSubscription(subscription: Subscription) {
    subscriptions.add(subscription)
  }

  override fun onCleared() {
    subscriptions.forEach {
      it.cancel()
    }
    super.onCleared()
  }
}