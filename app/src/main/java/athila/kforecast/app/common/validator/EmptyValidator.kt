package athila.kforecast.app.common.validator

class EmptyValidator : Validator<String> {
  override fun isValid(target: String): Boolean = target.isNotEmpty()
}
