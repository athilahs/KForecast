package athila.kforecast.screens.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import athila.kforecast.R

class BaseActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.base_activity)
  }
}
