package formless

import cats.data.Validated
import org.scalatest._

class FormBuidlerSpec extends FreeSpec with Matchers {
  "string builder" - {
    "create" in {
      val actual   = FormBuilder[String].create("Test", PNil)
      val expected = Field(PNil, "/", TextInput(Some("Test")))
      actual should equal(expected)
    }

    "extract value" in {
      val form     = Field(PNil, "/", TextInput(Some("Replaced")))
      val actual   = FormBuilder[String].extract("Original", form, PNil)
      val expected = Validated.Valid("Replaced")
      actual should equal(expected)
    }

    "extract blank" in {
      val form     = Field(PNil, "/", TextInput(None))
      val actual   = FormBuilder[String].extract("Original", form, PNil)
      val expected = Validated.Valid("Original")
      actual should equal(expected)
    }
  }

  "generic builder" - {
    case class Person(name: String, age: Int, developer: Boolean)

    def createForm(name: Option[String], age: Option[Int], developer: Option[Boolean]) = Group(List(
      Field( PNil / "name"      , "/name/"      , TextInput(name)         ),
      Field( PNil / "age"       , "/age/"       , IntInput(age)           ),
      Field( PNil / "developer" , "/developer/" , BooleanInput(developer) )
    ))

    "create" in {
      val person   = Person("Dave", 37, true)
      val actual   = FormBuilder[Person].create(person, PNil)
      val expected = createForm(Some("Dave"), Some(37), Some(true))
      actual should equal(expected)
    }

    "extract" in {
      val person   = Person("Dave", 37, true)
      val form     = createForm(Some("Garfield"), None, Some(false))
      val actual   = FormBuilder[Person].extract(person, form, PNil)
      val expected = Validated.Valid(Person("Garfield", 37, false))
      actual should equal(expected)
    }
  }
}