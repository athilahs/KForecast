package athila.kforecast.app.common

import android.arch.lifecycle.LiveData

class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
  init {
    // use post instead of set since this can be created on any thread
    postValue(null)
  }

  companion object {
    fun <T> create(): LiveData<T> {
      return AbsentLiveData()
    }
  }
}