package athila.kforecast.app.common.validator

class PasswordValidator : Validator<String> {
  override fun isValid(target: String): Boolean = target.length > 2
}
