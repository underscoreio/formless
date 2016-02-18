package formless
package data

sealed abstract class AnswerField
final case class TextField(contents: String) extends AnswerField
final case class Dropdown(options: List[String]) extends AnswerField
