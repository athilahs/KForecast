package athila.kforecast.app.common

interface BaseAdapterPresenter<in Data, in ItemView> {
  fun itemsCount(): Int
  fun onBindItemAtPosition(position: Int, view: ItemView)
  fun setData(data: Data?)
}
