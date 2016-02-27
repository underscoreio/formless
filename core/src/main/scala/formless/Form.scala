package formless

import cats.Monoid

sealed trait Form {
  def findField(path: Path): Option[Field] = this match {
    case g : Group  => g.subforms.foldLeft(Option.empty[Field])(_ orElse _.findField(path))
    case f : Field  => if(f.path == path) Some(f) else None
    case s : Static => None
  }

  def ++(that: Form): Form = (this, that) match {
    case (a: Group, b: Group) => Group(a.subforms ++ b.subforms)
    case (a: Group, b       ) => Group(a.subforms :+ b)
    case (a       , b: Group) => Group(a +: b.subforms)
    case (a       , b       ) => Group(Seq(a, b))
  }
}

object Form {
  val empty = Group(Nil)

  implicit val formMonoid = new Monoid[Form] {
    def empty: Form = Form.empty
    def combine(a: Form, b: Form): Form = a ++ b
  }
}

final case class Group(subforms: Seq[Form]) extends Form
final case class Field(path: Path, label: String, input: Input) extends Form
final case class Static(html: String) extends Form

sealed trait Input
final case class TextInput(value: Option[String]) extends Input
final case class IntInput(value: Option[Int]) extends Input
final case class DoubleInput(value: Option[Double]) extends Input
final case class BooleanInput(value: Option[Boolean]) extends Input
final case class SelectOne(value: Option[String], options: Seq[SelectOption]) extends Input
final case class SelectMany(value: Option[String], options: Seq[SelectOption]) extends Input

final case class SelectOption(value: String, label: String)
