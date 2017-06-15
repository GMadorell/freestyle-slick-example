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
import freestyle.logging._
import freestyle.loggingJVM.implicits._

import _root_.slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

import dao.Tables._

object Index {

  implicit val db: Database =
    Database.forConfig("postgres")

  def main(args: Array[String]): Unit =
    Await.result(print.interpret[Future], Duration.Inf)

  def insertUser(userdata: UserdataRow): DBIO[String] =
    (Userdata returning Userdata.map(_.email)) += userdata

  def insertAddress(useraddress: UseraddressRow): DBIO[String] =
    (Useraddress returning Useraddress.map(_.useremail)) += useraddress

  def getUser(email: String): DBIO[UserdataRow] =
    Userdata.filter(_.email === email).result.head

  def getAddress(email: String): DBIO[UseraddressRow] =
    (for {
      users   ← Userdata
      address ← Useraddress if users.email === email && users.email === address.useremail
    } yield address).result.head

  def updateUser(email: String, username: String, age: Option[Int]): DBIO[Int] =
    Userdata.filter(_.email === email).map(p => (p.username, p.age)).update((username, age))

  def deleteUser(email: String): DBIO[Int] =
    Userdata.filter(_.email === email).delete

  def listUser: DBIO[List[UserdataRow]] =
    Userdata.result.map[List[UserdataRow]](_.toList)

  def print[F[_]: SlickM]: FreeS[F, Unit] = {
    for {
      email        ← insertUser(UserdataRow("a@g.com", "a", Some(12))).liftFS[F]
      user         ← getUser(email).liftFS[F]
      _            ← LoggingM[F].info(s"Added $user")
      numUpdates   ← updateUser(user.email, "ar", Some(24)).liftFS[F]
      _            ← LoggingM[F].info(s"Updates: $numUpdates")
      userList     ← listUser.liftFS[F]
      _            ← LoggingM[F].info(s"Users $userList")
      addressEmail ← insertAddress(UseraddressRow(user.email, "baker", "London", "UK")).liftFS[F]
      address      ← getAddress(addressEmail).liftFS[F]
      _            ← LoggingM[F].info(s"Added $address")
      numDeletes   ← deleteUser(user.email).liftFS[F]
      _            ← LoggingM[F].info(s"Deletes: $numDeletes")
    } yield ()
  }
}
