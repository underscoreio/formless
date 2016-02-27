package formless

import cats.data.Validated
import cats.std.list._
import cats.syntax.cartesian._
import shapeless._
import shapeless.labelled._

trait FormBuilder[A] {
  def create(data: A, path: Path): Form
  def extract(data: A, form: Form, path: Path): Validated[List[Error], A]
}

object FormBuilder extends FormBuilders {
  def apply[A](implicit builder: FormBuilder[A]): FormBuilder[A] =
    builder
}

trait FormBuilders extends LowPriorityFormBuilders {
  def inputFormBuilder[A](createInput: A => Input)(extractInput: PartialFunction[Input, Option[A]]) = new FormBuilder[A] {
    def create(data: A, path: Path) =
      Field(path, path.toString, createInput(data))

    def extract(data: A, form: Form, path: Path) =
      (form findField path map (_.input) flatMap extractInput.lift) match {
        case Some(value) => Validated.Valid(value getOrElse data)
        case None        => Validated.Invalid(List(s"Field not found: ${path}"))
      }
  }

  implicit val stringFormBuilder: FormBuilder[String] =
    inputFormBuilder[String](str => TextInput(Some(str))) { case TextInput(value) => value }

  implicit val intFormBuilder: FormBuilder[Int] =
    inputFormBuilder[Int](str => IntInput(Some(str))) { case IntInput(value) => value }

  implicit val doubleFormBuilder: FormBuilder[Double] =
    inputFormBuilder[Double](str => DoubleInput(Some(str))) { case DoubleInput(value) => value }

  implicit val booleanFormBuilder: FormBuilder[Boolean] =
    inputFormBuilder[Boolean](str => BooleanInput(Some(str))) { case BooleanInput(value) => value }
}

trait LowPriorityFormBuilders {
  implicit val hnilFormBuilder: FormBuilder[HNil] = new FormBuilder[HNil] {
    def create(data: HNil, path: Path): Form = Form.empty
    def extract(data: HNil, form: Form, path: Path): Validated[List[Error], HNil] = Validated.Valid(HNil)
  }

  implicit def hconsFormBuilder[Name <: Symbol, Head, Tail <: HList](
    implicit
    witness: Witness.Aux[Name],
    headBuilder: FormBuilder[Head],
    tailBuilder: FormBuilder[Tail]
  ): FormBuilder[FieldType[Name, Head] :: Tail] = new FormBuilder[FieldType[Name, Head] :: Tail] {
    def create(data: FieldType[Name, Head] :: Tail, path: Path): Form = data match {
      case head :: tail =>
        val headForm = headBuilder.create(head, path / witness.value.name)
        val tailForm = tailBuilder.create(tail, path)
        headForm ++ tailForm
    }

    def extract(data: FieldType[Name, Head] :: Tail, form: Form, path: Path): Validated[List[Error], FieldType[Name, Head] :: Tail] = data match {
      case head :: tail =>
        val headResult = headBuilder.extract(head, form, path / witness.value.name).map(field[Name](_))
        val tailResult = tailBuilder.extract(tail, form, path)
        (headResult |@| tailResult) map (_ :: _)
    }

    def extract(data: HNil, form: Form, path: Path) = Validated.Valid(HNil)
  }

  implicit def genericFormBuilder[A, B <: HList](
    implicit
    generic: LabelledGeneric.Aux[A, B],
    builder: FormBuilder[B]
  ): FormBuilder[A] = new FormBuilder[A] {
    def create(data: A, path: Path): Form =
      builder.create(generic.to(data), path)

    def extract(data: A, form: Form, path: Path): Validated[List[Error], A] =
      builder.extract(generic.to(data), form, path).map(generic.from)
  }
}
