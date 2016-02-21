package formless
package http4s

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object Service {
  import formless.data._
  val dummy =
    Form(
      Title("test"),
      Description("a description"),
      List(Field(Title("Name"), None, Some(Text("", TextLine))),
           Field(Title("Email Address"), None, Some(Text("", TextLine))),
           Field(Title("Bio"), None, Some(Text("", TextBox))))
    )


  val service = HttpService {
    case GET -> Root / "view" / uuid => Ok(dummy.asJson)
  }
}
