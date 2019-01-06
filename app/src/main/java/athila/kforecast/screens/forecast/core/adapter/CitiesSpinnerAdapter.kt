package athila.kforecast.screens.forecast.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import athila.kforecast.R
import athila.kforecast.app.database.entity.City

class CitiesSpinnerAdapter(context: Context) : ArrayAdapter<City>(context, R.layout.cities_spinner) {

  //  private val cities: MutableList<City> = mutableListOf()
  init {
    // notify changes manually as we want to clear the list and re-add them before notifying
    setNotifyOnChange(false)
  }

  fun setCities(cities: List<City>?) {
    clear()
    addAll(cities)
    notifyDataSetChanged()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getViewWithLayout(position, convertView, parent, R.layout.cities_spinner)
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getViewWithLayout(position, convertView, parent, R.layout.cities_spinner_dropdown_row)
  }

  private fun getViewWithLayout(position: Int, convertView: View?, parent: ViewGroup, layoutResId: Int): View {
    val view: View
    val viewHolder: ViewHolder
    // Get the data item for this position
    val city = getItem(position)
    if (convertView == null) {
      viewHolder = ViewHolder()
      val inflater = LayoutInflater.from(context)
      view = inflater.inflate(layoutResId, parent, false)
      viewHolder.cityName = view.findViewById(R.id.cities_spinner_city_name) as TextView
      view.tag = viewHolder
    } else {
      view = convertView
      viewHolder = view.tag as ViewHolder
    }

    viewHolder.cityName.text = city.name
    return view
  }

  private class ViewHolder {
    internal lateinit var cityName: TextView
  }
}
