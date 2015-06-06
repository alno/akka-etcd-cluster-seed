package akka.cluster.seed

import scala.concurrent.duration._
import akka.actor._

class EtcdClusterSeedSettings(system: ActorSystem) {

  private val conf = system.settings.config.getConfig("akka.cluster.seed.etcd")

  val url = conf.getString("url")
  val path = conf.getString("path")

  val seedTtl = conf.getInt("seed-ttl") seconds

  val clientTimeout = conf.getInt("client-timeout") seconds

}
