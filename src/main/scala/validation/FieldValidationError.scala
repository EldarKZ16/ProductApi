package validation

case class FieldValidationError(name: String, message: String) {
  override def toString: String = s"$name $message"
}
