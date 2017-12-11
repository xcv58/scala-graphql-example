name := "Scala GraphQL Example"
organization := "com.xcv58"
version := "0.0.1"

description := "Scala GraphQL Example"
licenses := Seq("MIT License" → url("https://github.com/xcv58/scala-graphql-example/blob/master/LICENSE"))

lazy val versions = new {
  val logback = "1.1.7"
}

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Xlint",
  "-Xlint:-missing-interpolator")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "com.twitter" %% "finatra-http" % "17.11.0",
  "org.sangria-graphql" %% "sangria" % "1.3.3",
  "org.sangria-graphql" %% "sangria-json4s-jackson" % "1.0.0",
  "org.json4s" %% "json4s-jackson" % "3.5.3",

  // testing
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "eu.timepit" %% "refined" % "0.8.4" % Test
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oF")
