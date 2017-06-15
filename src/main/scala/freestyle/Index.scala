/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package freestyle

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

  implicit val db: Database =
    Database.forConfig("postgres")

  def main(args: Array[String]): Unit = {
    printFuture(insertUser(email = "a@g.com", username = "a", age = Some(12)).interpret[Future])
    printFuture(getUser("a@g.com").interpret[Future])
    printFuture(updateUser("a@g.com", "ar", Some(24)).interpret[Future])
    printFuture(listUser.interpret[Future])
    printFuture(deleteUser("a@g.com").interpret[Future])
  }

  def getUser(email: String) =
    SlickM[SlickM.Op].run[UserdataRow](Userdata.filter(_.email === email).result.head)

  def insertUser(email: String, username: String, age: Option[Int]) = {
    SlickM[SlickM.Op].run[String](
      (Userdata returning Userdata
        .map(_.email)) += UserdataRow(email, username, age))
  }

  def updateUser(email: String, username: String, age: Option[Int]) = {
    SlickM[SlickM.Op].run[Int](
      Userdata.filter(_.email === email).map(p => (p.username, p.age)).update((username, age)))
  }

  def deleteUser(email: String) =
    SlickM[SlickM.Op].run[Int](Userdata.filter(_.email === email).delete)

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
