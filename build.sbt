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
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.2",

  "io.swagger.core.v3" % "swagger-annotations" % "2.2.8",
  "com.github.dwickern" %% "swagger-play2.8" % "3.1.0",
  "io.swagger" % "swagger-core" % "1.6.11",

  // https://mvnrepository.com/artifact/com.jason-goodwin/authentikat-jwt
  "com.pauldijou" %% "jwt-play-json" % "5.0.0",

  // https://mvnrepository.com/artifact/com.microsoft.azure/msal4j
  "com.microsoft.azure" % "msal4j" % "1.13.5",

  // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
  "org.mindrot" % "jbcrypt" % "0.4",

  // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
  "io.jsonwebtoken" % "jjwt-api" % "0.11.5",
  "io.jsonwebtoken" % "jjwt-impl" % "0.11.5",
  "io.jsonwebtoken" % "jjwt-jackson" % "0.11.5",


)


