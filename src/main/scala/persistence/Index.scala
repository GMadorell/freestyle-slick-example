/**
 * https://github.com/AdrianRaFo
 */
package persistence

import cats.implicits._

import freestyle._
import freestyle.implicits._
import freestyle.slick._
import freestyle.slick.implicits._

import _root_.slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.util.{Failure, Success}

import dao.Tables._

object Index {

  implicit val db: Database = Database.forConfig("postgres")

  def main(args: Array[String]): Unit = {

    printFuture(insertUser.interpret[Future])
    printFuture(getUser("a@g.com").interpret[Future])
    printFuture(listUser.interpret[Future])
  }

  def getUser(email: String) =
    SlickM[SlickM.Op].run[UserdataRow](Userdata.filter(_.email === email).result.head)

  def insertUser = {
    SlickM[SlickM.Op].run[String](
      (Userdata returning Userdata
        .map(_.email)) += UserdataRow(email = "a@g.com", username = "a", age = Some(12)))
  }

  def listUser =
    SlickM[SlickM.Op].run[List[UserdataRow]](Userdata.result.map[List[UserdataRow]](_.toList))

  def printFuture[F](future: Future[F]): Unit = {

    Await.result(future, Duration.Inf)
    future onComplete {
      case Success(result) ⇒ println(s"Result: $result")
      case Failure(e)      ⇒ println(s"Error: ${e.getMessage}")
    }
  }
}
