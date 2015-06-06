package akka.cluster.seed

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import akka.actor._

class EtcdClusterSeedSettings(system: ActorSystem) {

  private val conf = system.settings.config.getConfig("akka.cluster.seed.etcd")

  val url = conf.getString("url")
  val path = conf.getString("path")

  val seedTtl = conf.getDuration("seed-ttl", TimeUnit.MILLISECONDS).millis

  val clientTimeout = conf.getDuration("client-timeout", TimeUnit.MILLISECONDS).millis

}
