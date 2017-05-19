name := "Scala GraphQL Example"
organization := "com.xcv58"
version := "0.0.1"

description := "Scala GraphQL Example"
licenses := Seq("MIT License" → url("https://github.com/xcv58/scala-graphql-example/blob/master/LICENSE"))

lazy val versions = new {
  val logback = "1.1.7"
}

scalaVersion := "2.12.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Xlint",
  "-Xlint:-missing-interpolator")

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % "2.10.0",
  "org.sangria-graphql" %% "sangria" % "1.2.1",
  "ch.qos.logback" % "logback-classic" % versions.logback,

  // testing
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "eu.timepit" %% "refined" % "0.7.0" % Test
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oF")
