name := "futures"

version := "0.1-SNAPSHOT"

organization := "com.github.kristofa"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2-core" % "2.4.15" % "test",
        "com.twitter" %% "util-core" % "6.23.0" 
)
