package akka.cluster.seed

import scala.concurrent.duration._
import akka.actor._
import akka.testkit._
import akka.remote.testkit._
import akka.cluster.ClusterEvent._
import akka.cluster.Cluster
import org.scalatest._
import com.typesafe.config._

object EtcdClusterSeedConfig extends MultiNodeConfig {

  for (i <- 1 to 3)
    role("node" + i)

  commonConfig(ConfigFactory.parseString("""
      akka.actor.provider = "akka.cluster.ClusterActorRefProvider"
      akka.loggers = ["akka.event.slf4j.Slf4jLogger"]
      akka.cluster.seed.etcd.client-timeout = 30
      akka.test.single-expect-default = 30s
    """))
}

class EtcdClusterSeedSpec extends MultiNodeSpec(EtcdClusterSeedConfig) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def beforeAll() = multiNodeSpecBeforeAll()
  override def afterAll() = multiNodeSpecAfterAll()

  override def initialParticipants = roles.size

  "EtcdClusterSeed extension" should {
    "bootstrap a cluster properly" in {
      Cluster(system).subscribe(testActor, classOf[MemberUp])

      expectMsgClass(classOf[CurrentClusterState])

      EtcdClusterSeed(system).join()

      for (i <- 1 to roles.size)
        expectMsgClass(classOf[MemberUp])

      enterBarrier("up")

      Cluster(system).readView.members.size shouldBe roles.size
    }
  }

}

class EtcdClusterSeedMultiJvmNode1 extends EtcdClusterSeedSpec
class EtcdClusterSeedMultiJvmNode2 extends EtcdClusterSeedSpec
class EtcdClusterSeedMultiJvmNode3 extends EtcdClusterSeedSpec
