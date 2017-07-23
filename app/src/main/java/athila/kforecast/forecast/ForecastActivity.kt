package athila.kforecast.forecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import athila.kforecast.app.KForecastApplication
import athila.kforecast.forecast.core.ForecastContract
import athila.kforecast.forecast.di.DaggerForecastComponent
import athila.kforecast.forecast.di.ForecastModule
import javax.inject.Inject

class ForecastActivity : AppCompatActivity() {

  @Inject
  lateinit var forecastView: ForecastContract.View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DaggerForecastComponent.builder()
        .applicationComponent((application as KForecastApplication).getApplicationComponent())
        .forecastModule(ForecastModule(this))
        .build()
        .inject(this)

    setContentView(forecastView.getView())

    // TODO: just a test. Remove it
    forecastView.showEmptyView()
  }
}
