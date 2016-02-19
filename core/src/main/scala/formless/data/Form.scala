package formless
package data

final case class Title(get: String) extends AnyVal
final case class Form(title: Title, introduction: String, fields: List[Field])
