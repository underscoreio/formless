package formless
package data

final case class Title(get: String) extends AnyVal
final case class Description(get: String) extends AnyVal

final case class Form(title: Title, description: Description, fields: List[Field])

final case class Field(title: Title, description: Option[Description], answer: Option[Answer])

sealed abstract class Answer
final case class Text(contents: String) extends Answer
sealed abstract class MultipleChoice extends Answer {
  def options: List[String]
}
final case class ChooseOne(options: List[String], rendering: SingleChoiceRendering) extends MultipleChoice
final case class ChooseMany(options: List[String]) extends MultipleChoice

sealed trait SingleChoiceRendering
final case object Dropdown extends SingleChoiceRendering
final case object RadioButtons extends SingleChoiceRendering