package model

import org.joda.time.DateTime
import validation.Validator

case class ProductEntity(name: String, vendor: String, price: Int, expirationDate: Option[DateTime] = None)

object ProductEntity {
  import cats.implicits._
  import validation.FieldValidator._

  val validator: Validator[ProductEntity] = (product: ProductEntity) => {
    MaxLengthLessThanTenValidator.validate(product.name, "Product name") |+|
    MaxLengthLessThanTenValidator.validate(product.vendor, "Vendor name") |+|
    PositiveValueValidator.validate(product.price, "Price") |+|
    TimeIsAfterNowValidator.validate(product.expirationDate, "Expiration Date")
  }

}