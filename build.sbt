name := "freestyle-slick-example"

version := "1.0"

scalaVersion := "2.12.2"

scalacOptions += "-Ywarn-unused-import"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "org.postgresql"     % "postgresql"       % "42.1.1",
  "com.typesafe.slick" %% "slick"           % "3.2.0",
  "com.typesafe.slick" %% "slick-codegen"   % "3.2.0",
  "io.frees"           %% "freestyle"       % "0.3.0",
  "io.frees"           %% "freestyle-slick" % "0.3.0"
)

slick := slickCodeGenTask.value // register manual sbt command

lazy val slick = TaskKey[Seq[File]]("slick-gen")
lazy val slickCodeGenTask = Def.task {
  val outputDir = (sourceDirectory.value / "main/scala").getPath
  toError(
    (runner in Compile).value.run(
      "slick.codegen.SourceCodeGenerator",
      (dependencyClasspath in Compile).value.files,
      Array(
        "slick.jdbc.PostgresProfile",
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost/postgres?currentSchema=public",
        outputDir,
        "dao",
        "test",
        "test"
      ),
      streams.value.log
    ))
  Seq(file(s"$outputDir/dao/Tables.scala"))
}
