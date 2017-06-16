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

import _root_.slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

import dao.Tables._

object persistence {

  implicit val db: Database =
    Database.forConfig("postgres")

  def createSchema: DBIO[Unit] = schema.create

  def dropSchema: DBIO[Unit] = schema.drop

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

  def listUser(implicit executionContext: ExecutionContext): DBIO[List[UserdataRow]] =
    Userdata.result.map[List[UserdataRow]](_.toList)
}
