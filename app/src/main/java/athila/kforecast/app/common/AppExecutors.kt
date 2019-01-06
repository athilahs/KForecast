package athila.kforecast.app.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object AppExecutors {
  val mainThread: CoroutineContext = Dispatchers.Main
  val bgThread: CoroutineContext = Dispatchers.IO
}