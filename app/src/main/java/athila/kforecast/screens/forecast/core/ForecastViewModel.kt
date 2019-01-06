package athila.kforecast.screens.forecast.core

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import athila.kforecast.app.common.AbsentLiveData
import athila.kforecast.app.common.Resource
import athila.kforecast.app.database.entity.City
import athila.kforecast.app.database.entity.Forecast
import athila.kforecast.screens.common.BaseViewModel
import athila.kforecast.screens.common.repository.CitiesRepository
import athila.kforecast.screens.forecast.repository.ForecastRepository
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val forecastRepository: ForecastRepository,
    private val citiesRepository: CitiesRepository) : BaseViewModel(),
    ForecastContract.ViewModel {

  // initialized with null emission
  override var forecastLiveData: LiveData<Resource<Forecast>> = AbsentLiveData.create()

  // TODO: implement logic similar to the forecast in cities repo when getting cities from remote source is available
  private val _citiesLiveData: MediatorLiveData<List<City>> = MediatorLiveData()

  override val citiesLiveData: LiveData<List<City>> = _citiesLiveData
  private val _selectedCity: MutableLiveData<City> = MutableLiveData()

  init {
    launch {
      _citiesLiveData.addSource(citiesRepository.getCities()) {
        _citiesLiveData.value = it
      }
    }

    forecastLiveData = Transformations.switchMap(_selectedCity) {
      Log.e("ATHILA", "ATHILA - switchMap function called. selectedCity = $it")
      it?.apply {
        launch {
          forecastRepository.getForecast(this@apply)
        }
      }
      forecastRepository.forecastForCityLiveData
    }
  }

  override fun setSelectedCity(city: City) {
    if (_selectedCity.value != city) {
      _selectedCity.value = city
    }
  }

  override fun refreshForecast() {
    _selectedCity.value?.let {
      launch { forecastRepository.fetchForecast(it) }
    }
  }


  // Test
  override fun insertRandomCity() {
    val r = Random()
    val minLongitude = -8.932439
    val maxLongitude = -6.060655
    val minLatitude = 51.646024
    val maxLatitude = 55.115816

    val randomLatitude = minLatitude + (maxLatitude - minLatitude) * r.nextDouble()
    val randomLongitude = minLongitude + (maxLongitude - minLongitude) * r.nextDouble()

    val randomInt = (0..500).random()
    val randomCity = City(name = "Rand ($randomInt)", latitude = randomLatitude,
        longitude = randomLongitude)

    launch {
      val inserted = citiesRepository.insertCity(randomCity)
      println("ES!! $inserted")
    }
  }
}

fun ClosedRange<Int>.random() =
    Random().nextInt((endInclusive + 1) - start) + start
