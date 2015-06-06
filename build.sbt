organization := "com.github.alno"

name := "akka-etcd-cluster-seed"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

// Enable multi-jvm
SbtMultiJvm.multiJvmSettings

configs(MultiJvm)

// Library dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.3.9",
  "net.nikore.etcd" %% "scala-etcd" % "0.8"
)

// Test dependencies
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test,multi-jvm",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test,multi-jvm",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.3.9" % "multi-jvm",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.9" % "multi-jvm"
)

// Make sure that MultiJvm test are compiled by the default test compilation
compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test)

// Disable parallel tests
parallelExecution in Test := false

// Make sure that MultiJvm tests are executed by the default test target,
// and combine the results from ordinary test and multi-jvm tests
executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
  case (testResults, multiNodeResults) =>
    val overall =
      if (testResults.overall.id < multiNodeResults.overall.id)
        multiNodeResults.overall
      else
        testResults.overall
    Tests.Output(overall,
      testResults.events ++ multiNodeResults.events,
      testResults.summaries ++ multiNodeResults.summaries)
}
