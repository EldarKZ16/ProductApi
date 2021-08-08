import cats.effect._
import com.typesafe.config.{Config, ConfigFactory}
import org.http4s._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import repository.InMemoryProductRepoImpl
import routes.ProductRoutes

import scala.concurrent.ExecutionContext.global

object Boot extends IOApp {

  val config: Config = ConfigFactory.load()

  val inMemoryProductRepo = new InMemoryProductRepoImpl
  val host: String = config.getString("app.host")
  val port: Int = config.getInt("app.port")

  def allRoutes: HttpApp[IO] = {
    ProductRoutes.productRoutes(inMemoryProductRepo).orNotFound
  }

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(port, host)
      .withHttpApp(allRoutes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
