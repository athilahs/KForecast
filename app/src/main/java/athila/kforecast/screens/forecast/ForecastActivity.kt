package athila.kforecast.screens.forecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import athila.kforecast.app.KForecastApplication
import athila.kforecast.screens.forecast.core.ForecastContract
import athila.kforecast.screens.forecast.di.DaggerForecastComponent
import athila.kforecast.screens.forecast.di.ForecastModule
import javax.inject.Inject

class ForecastActivity : AppCompatActivity() {

  @Inject
  lateinit var forecastView: ForecastContract.View

  @Inject
  lateinit var forecastPresenter: ForecastContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DaggerForecastComponent.builder()
        .applicationComponent((application as KForecastApplication).getApplicationComponent())
        .forecastModule(ForecastModule(this))
        .build()
        .inject(this)

    setContentView(forecastView.getView())

    forecastView.setPresenter(forecastPresenter)
    forecastPresenter.onCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    forecastPresenter.onStart()
  }

  override fun onStop() {
    super.onStop()
    forecastPresenter.onStop()
  }
}
