package athila.kforecast.screens.common

import android.os.Bundle

/**
 * Created by athila on 27/06/16.
 */

interface BasePresenterContract {
  fun onCreate(savedInstanceState: Bundle?) {}
  fun onStart() {}
  fun onResume() {}
  fun onPause() {}
  fun onStop() {}
  fun onDestroy() {}
}
