package athila.kforecast.app.common.validator

class CoordinateValidator : Validator<String> {

  override fun isValid(target: String): Boolean {
    // coordinates just need to be a double
    try {

      java.lang.Double.parseDouble(target)
      return true
    } catch (ex: NumberFormatException) {
      return false
    }

  }
}
