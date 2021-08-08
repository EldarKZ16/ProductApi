package validation

import cats.data.NonEmptyList
import cats.implicits._
import org.joda.time.DateTime

trait Validator[T] {
  import FieldValidator._
  def validate(target: T): ValidationResult
}

object Validator {
  case class ValidationErrors(errors: NonEmptyList[String])
}

trait FieldValidator[T] {
  import FieldValidator._
  def validate(target: T, fieldName: String): ValidationResult
}

object FieldValidator {
  type ValidationResult = Option[NonEmptyList[FieldValidationError]]

  case object MaxLengthLessThanTenValidator extends FieldValidator[String] {
    override def validate(target: String, fieldName: String): ValidationResult = {
      if (target.length > 10) NonEmptyList.of(FieldValidationError(fieldName, s"is too long, should be less than 10")).some else None
    }
  }

  case object PositiveValueValidator extends FieldValidator[Int] {
    override def validate(target: Int, fieldName: String): ValidationResult = {
      if (target <= 0) NonEmptyList.of(FieldValidationError(fieldName, s"cannot be negative")).some else None
    }
  }

  case object TimeIsAfterNowValidator extends FieldValidator[Option[DateTime]] {
    override def validate(target: Option[DateTime], fieldName: String): ValidationResult = target.flatMap { currentDate =>
      if (currentDate.isBeforeNow) NonEmptyList.of(FieldValidationError(fieldName, s"is invalid, should be in the future time")).some else None
    }
  }
}


