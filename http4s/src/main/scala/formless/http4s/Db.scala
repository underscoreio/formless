package formless
package http4s

object Db {
  final case class Signup(name: String, email: String)

  val signup: Form =
    FormBuilder[Signup].create(Signup("",""), PNil)

  def get(name: String): Option[Form] =
    name match {
      case "signup" => Some(signup)
      case _ => None
    }
}
