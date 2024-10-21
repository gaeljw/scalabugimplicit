package example

import scala.collection.Seq

trait DataTableOptionalEntryDefinitionBody[T] {

  def transform(entry: Map[String, Option[String]]): T

}

case class ScalaDataTableOptionalEntryTypeDetails[T](body: DataTableOptionalEntryDefinitionBody[T])

trait DataTableDsl {

  protected var registry: Seq[ScalaDataTableOptionalEntryTypeDetails[_]] = Seq.empty

  def DataTableType: DataTableTypeBody = new DataTableTypeBody()

  final class DataTableTypeBody {

    def apply[T](body: DataTableOptionalEntryDefinitionBody[T]): Unit = {
      registry = registry :+ ScalaDataTableOptionalEntryTypeDetails[T](body)
    }

  }

}

case class Hello(x: Option[String])

class HelloSpec extends munit.FunSuite with DataTableDsl {

  DataTableType { implicit entry: Map[String, Option[String]] =>
    val entryValue: Option[String] = entry("x")
    // isDefined is not a method of String, why does it even compile?
    val x = entryValue.filter(_.isDefined)
    Hello(x)
  }

  test("say hello") {
    val hello = registry.head.body.transform(Map("x" -> Some("pouet")))
    assertEquals(hello, Hello(Some("pouet")))
  }

}
