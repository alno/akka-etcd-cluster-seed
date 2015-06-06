organization := "com.github.alno"

name := "akka-etcd-cluster-seed"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

// Library dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.3.9",
  "net.nikore.etcd" %% "scala-etcd" % "0.8"
)

// Test dependencies
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test"
)
