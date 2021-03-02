package lectures.part4Implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age) <a href=$email/> </div>"
  }

  User("John", 32, "john@mail.com").toHtml
  /*
    1 - for the types WE write
    2 - ONE implementation ou of quite a number
   */

  //option 2 - pattern matching
  object HTMLSerializePM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  /*
    1 - lost the type safety
    2 - need to modify the code every time
    3 - still ONE implementation
   */

  //better design
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>$user.name ($user.age) <a href=$user.email/> </div>"
  }

  val john = User("John", 32, "john@mail.com")
  println(UserSerializer.serialize(john))

  //1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  //2 - we can defini MULTIPLE serializers
  object PartialUtilSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  //TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /**
   * Equility
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }


  //part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  //access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))

  /*
    Exercise - implement the TypeClass pattern for the Equality type class
   */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val anotherJohn = User("John", 45, "anotherJohn@email.com")
  //println(Equal.apply(john, anotherJohn))
  println(Equal(john, anotherJohn))

  //AD-HOC polymorphism

}
