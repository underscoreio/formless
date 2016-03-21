package formless
package http4s

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._

import io.circe.generic.auto._
import io.circe.syntax._

object Service {
  def view = HttpService {
    case GET -> Root / "view" / name =>
      Db.get(name) match {
        case Some(form) => Ok(form.asJson)
        case None => NotFound(s"I couldn't find a form with the name $name")
      }
  }
}
