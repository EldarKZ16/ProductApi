package validation

import cats.data.NonEmptyList
import model.ProductEntity
import org.joda.time.DateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ValidatorSpec
  extends AnyFlatSpec
    with Matchers {

  behavior of "Validator"

  val productValidator: Validator[ProductEntity] = ProductEntity.validator
  val productWithNegativePrice = ProductEntity("test", "test", -100)
  val productWithLongName = ProductEntity("testtesttest", "test", 100)
  val productWithWrongExpirationDate = ProductEntity("test", "test", 100, Some(DateTime.now().minusDays(1)))
  val productWithLongVendorNameAndNegativePrice = ProductEntity("test", "testtesttest", -10000)

  it should "validate the negative price" in {
    val result = productValidator.validate(productWithNegativePrice)

    result shouldBe Some(NonEmptyList.of(FieldValidationError("Price","cannot be negative")))
  }

  it should "validate the max length of product name" in {
    val result = productValidator.validate(productWithLongName)

    result shouldBe Some(NonEmptyList.of(FieldValidationError("Product name","is too long, should be less than 10")))
  }

  it should "validate the expiration date" in {
    val result = productValidator.validate(productWithWrongExpirationDate)

    result shouldBe Some(NonEmptyList.of(FieldValidationError("Expiration Date","is invalid, should be in the future time")))
  }

  it should "validate the max length of vendor name and negative price" in {
    val result = productValidator.validate(productWithLongVendorNameAndNegativePrice)

    result shouldBe Some(NonEmptyList.of(FieldValidationError("Vendor name","is too long, should be less than 10"), FieldValidationError("Price","cannot be negative")))
  }

}
