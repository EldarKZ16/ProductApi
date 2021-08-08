package routes

import cats.effect._
import io.circe.Json
import io.circe.generic.auto._
import model.ProductEntity
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl._
import repository.ProductRepository
import serialization.CirceHelper
import validation.Validator.ValidationErrors

object ProductRoutes extends CirceHelper {

  object VendorQueryParamMatcher extends QueryParamDecoderMatcher[String]("vendor")

  def productRoutes(repository: ProductRepository[IO]): HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case request @ POST -> Root / "products" / productId =>
        request.decode[ProductEntity] { product =>
          ProductEntity.validator.validate(product) match {
            case None =>
              repository.add(productId, product).flatMap { id =>
                Ok(Json.obj(("id", Json.fromString(id))))
              }
            case Some(validationErrors) =>
              BadRequest(ValidationErrors(validationErrors.map(_.toString)))
          }
        }

      case GET -> Root / "products" / productId =>
        repository.get(productId).flatMap {
          case Some(product) => Ok(product)
          case None => NotFound()
        }

      case GET -> Root / "products" :? VendorQueryParamMatcher(vendor) =>
        repository.getByVendor(vendor).flatMap(products => Ok(products))

      case DELETE -> Root / "products" / productId =>
        repository.delete(productId).flatMap {
          case Left(error) => NotFound(error)
          case Right(_) => Ok()
        }
    }
  }

}
