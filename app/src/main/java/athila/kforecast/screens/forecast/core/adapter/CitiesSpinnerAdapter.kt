package athila.kforecast.screens.forecast.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.database.entity.City

class CitiesSpinnerAdapter(context: Context) : ArrayAdapter<City>(context, R.layout.cities_spinner_row) {

  private val cities: MutableList<City> = mutableListOf()

  fun setCities(cities: List<City>?) {
    this.cities.let {
      clear()
      if (cities != null) {
        addAll(cities)
      }
    }
    notifyDataSetChanged()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: View
    val viewHolder: ViewHolder
    // Get the data item for this position
    val city = cities[position]
    if (convertView == null) {
      viewHolder = ViewHolder()
      val inflater = LayoutInflater.from(context)
      view = inflater.inflate(R.layout.cities_spinner_row, parent, false)
      viewHolder.cityName = view.findViewById<TextView>(R.id.cities_spinner_city_name) as TextView
      view.tag = viewHolder
    } else {
      view = convertView
      viewHolder = view.tag as ViewHolder
    }

    viewHolder.cityName.text = city.name
    return view
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getView(position, convertView, parent)
  }

  private class ViewHolder {
    internal lateinit var cityName: TextView
  }
}
