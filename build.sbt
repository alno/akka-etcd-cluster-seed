organization := "com.github.alno"

name := "akka-etcd-cluster-seed"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.3.9",
  "net.nikore.etcd" %% "scala-etcd" % "0.8"
)
