package repository

import cats.effect.IO
import cats.implicits._
import model.ProductEntity

import scala.collection.mutable

trait ProductRepository[F[_]] {
  def add(id: String, product: ProductEntity): F[String]

  def get(id: String): F[Option[ProductEntity]]

  def delete(id: String): F[Either[String, Unit]]

  def getByVendor(vendor: String): F[List[ProductEntity]]
}

class InMemoryProductRepoImpl extends ProductRepository[IO] {

  val products: mutable.Map[String, ProductEntity] = mutable.Map.empty[String, ProductEntity]

  override def add(id: String, product: ProductEntity): IO[String] = IO {
    products.put(id, product)
    id
  }

  override def get(id: String): IO[Option[ProductEntity]] = IO {
    products.get(id)
  }

  override def delete(id: String): IO[Either[String, Unit]] =
    for {
      removedProduct <- IO(products.remove(id))
      result = removedProduct.toRight(s"Product with id: $id not found").void
    } yield result

  override def getByVendor(vendor: String): IO[List[ProductEntity]] = IO {
    products.collect {
      case (_, product) if product.vendor == vendor => product
    }.toList
  }
}
