name := """connect-database"""
organization := "ltts.com"

version := "1.0-SNAPSHOT"

//swaggerDomainNameSpaces := Seq("models")
lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  javaJdbc,
  jdbc,
  guice,
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.8.18",
  "com.typesafe.play" %% "play-java" % "2.8.18",
  "com.typesafe.play" %% "play-guice" % "2.8.18",
  "com.typesafe.play" %% "filters-helpers" % "2.8.18",
  "com.google.inject" % "guice" % "5.1.0",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "7.0.0.jre8",
  "org.mockito" % "mockito-core" % "4.0.0" % "test",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.9.2" % "test",

  "org.projectlombok" % "lombok" % "1.18.26",
  "io.swagger.core.v3" % "swagger-annotations" % "2.2.8",
  "com.github.dwickern" %% "swagger-play2.8" % "3.1.0",
  "io.swagger" % "swagger-core" % "1.6.11",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.2"


)