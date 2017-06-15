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

package dao
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Useraddress.schema ++ Userdata.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Useraddress
   *  @param email Database column email SqlType(varchar), PrimaryKey, Length(64,true)
   *  @param street Database column street SqlType(varchar), Length(32,true)
   *  @param city Database column city SqlType(varchar), Length(32,true)
   *  @param country Database column country SqlType(varchar), Length(32,true) */
  final case class UseraddressRow(email: String, street: String, city: String, country: String)

  /** GetResult implicit for fetching UseraddressRow objects using plain SQL queries */
  implicit def GetResultUseraddressRow(implicit e0: GR[String]): GR[UseraddressRow] = GR { prs =>
    import prs._
    UseraddressRow.tupled((<<[String], <<[String], <<[String], <<[String]))
  }

  /** Table description of table useraddress. Objects of this class serve as prototypes for rows in queries. */
  class Useraddress(_tableTag: Tag)
      extends profile.api.Table[UseraddressRow](_tableTag, "useraddress") {
    def * = (email, street, city, country) <> (UseraddressRow.tupled, UseraddressRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(email), Rep.Some(street), Rep.Some(city), Rep.Some(country)).shaped.<>(
        { r =>
          import r._; _1.map(_ => UseraddressRow.tupled((_1.get, _2.get, _3.get, _4.get)))
        },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    /** Database column email SqlType(varchar), PrimaryKey, Length(64,true) */
    val email: Rep[String] = column[String]("email", O.PrimaryKey, O.Length(64, varying = true))

    /** Database column street SqlType(varchar), Length(32,true) */
    val street: Rep[String] = column[String]("street", O.Length(32, varying = true))

    /** Database column city SqlType(varchar), Length(32,true) */
    val city: Rep[String] = column[String]("city", O.Length(32, varying = true))

    /** Database column country SqlType(varchar), Length(32,true) */
    val country: Rep[String] = column[String]("country", O.Length(32, varying = true))

    /** Foreign key referencing Userdata (database name email_fk) */
    lazy val userdataFk = foreignKey("email_fk", email, Userdata)(
      r => r.email,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Useraddress */
  lazy val Useraddress = new TableQuery(tag => new Useraddress(tag))

  /** Entity class storing rows of table Userdata
   *  @param email Database column email SqlType(varchar), PrimaryKey, Length(64,true)
   *  @param username Database column username SqlType(varchar), Length(32,true)
   *  @param age Database column age SqlType(int4), Default(None) */
  final case class UserdataRow(email: String, username: String, age: Option[Int] = None)

  /** GetResult implicit for fetching UserdataRow objects using plain SQL queries */
  implicit def GetResultUserdataRow(
      implicit e0: GR[String],
      e1: GR[Option[Int]]): GR[UserdataRow] = GR { prs =>
    import prs._
    UserdataRow.tupled((<<[String], <<[String], <<?[Int]))
  }

  /** Table description of table userdata. Objects of this class serve as prototypes for rows in queries. */
  class Userdata(_tableTag: Tag) extends profile.api.Table[UserdataRow](_tableTag, "userdata") {
    def * = (email, username, age) <> (UserdataRow.tupled, UserdataRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(email), Rep.Some(username), age).shaped.<>({ r =>
        import r._; _1.map(_ => UserdataRow.tupled((_1.get, _2.get, _3)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column email SqlType(varchar), PrimaryKey, Length(64,true) */
    val email: Rep[String] = column[String]("email", O.PrimaryKey, O.Length(64, varying = true))

    /** Database column username SqlType(varchar), Length(32,true) */
    val username: Rep[String] = column[String]("username", O.Length(32, varying = true))

    /** Database column age SqlType(int4), Default(None) */
    val age: Rep[Option[Int]] = column[Option[Int]]("age", O.Default(None))
  }

  /** Collection-like TableQuery object for table Userdata */
  lazy val Userdata = new TableQuery(tag => new Userdata(tag))
}
