package akka.cluster.seed

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import akka.actor._
import akka.cluster.Cluster
import net.nikore.etcd.EtcdClient

object EtcdClusterSeed extends ExtensionId[EtcdClusterSeed] with ExtensionIdProvider {

  override def get(system: ActorSystem): EtcdClusterSeed = super.get(system)

  override def createExtension(system: ExtendedActorSystem): EtcdClusterSeed = new EtcdClusterSeed(system)

  override def lookup() = EtcdClusterSeed

}

class EtcdClusterSeed(system: ExtendedActorSystem) extends Extension {

  val settings = new EtcdClusterSeedSettings(system)

  val cluster = Cluster(system)

  val selfAddress = cluster.selfAddress

  private val etcdClient = new EtcdClient(settings.url)(system)
  private val etcdTimeout = 5 second
  private val etcdKey = s"${settings.path}/${selfAddress.hostPort}"

  // Register first seed
  Await.result(registerSeed(), etcdTimeout)

  // Reregister seed every ttl/2
  system.scheduler.schedule(settings.seedTtl / 2, settings.seedTtl / 2)(registerSeed())(system.dispatcher)

  // Unregister seed on exit
  system.registerOnTermination {
    unregisterSeed()
  }

  def join(): Unit = {
    cluster.joinSeedNodes(findSeedNodes)
  }

  def registerSeed(): Future[_] = {
    etcdClient.setKey(etcdKey, selfAddress.toString, Some(settings.seedTtl))
  }

  def unregisterSeed(): Future[_] = {
    etcdClient.deleteKey(etcdKey)
  }

  def findSeedNodes: List[Address] = {
    val response = Await.result(etcdClient.listDir(settings.path, false), etcdTimeout)

    for {
      nodes <- response.node.nodes.toList
      node <- nodes
      addr <- node.value
    } yield AddressFromURIString(addr)
  }

}
