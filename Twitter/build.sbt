name := "TwitterLogin"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
"org.twitter4j" % "twitter4j-core" % "4.0.4",
"org.twitter4j" % "twitter4j-stream" % "4.0.4"
)



play.Project.playJavaSettings
