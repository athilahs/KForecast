package athila.kforecast.screens.common

import android.arch.lifecycle.LifecycleOwner

interface HasLifecycleOwner {
  fun getLifecycleOwner(): LifecycleOwner
}