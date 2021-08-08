package serialization

import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

trait CirceHelper {

  private val dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
  implicit val encodeDateTime: Encoder[DateTime] = Encoder.instance(date => dateFormatter.print(date).asJson)
  implicit val decodeDateTime: Decoder[DateTime] = Decoder[String].map(date => dateFormatter.parseDateTime(date))

}
