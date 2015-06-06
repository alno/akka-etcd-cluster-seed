package akka.cluster.seed

import scala.concurrent.duration._
import akka.actor._
import akka.testkit._
import org.scalatest._
import com.typesafe.config._

class EtcdClusterSeedSettingsSpec extends TestKit(ActorSystem("test", EtcdClusterSeedSettingsSpec.config))
    with WordSpecLike with Matchers {

  "EtcdClusterSeedSettings" should {
    "parse options from config" in {
      val settings = new EtcdClusterSeedSettings(system)

      settings.seedTtl shouldBe 30.seconds
    }
  }
}

object EtcdClusterSeedSettingsSpec {
  val config: Config = ConfigFactory.parseString("""
         akka.cluster.seed.etcd {
           seed-ttl = 30
         }
       """)
}
