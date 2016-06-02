package chat

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.{Config, ConfigFactory}

object Main {
  def main(args: Array[String]): Unit = {

    val kryoConfig: Config = KryoSerializerConfig.akkaActorSerializationConfig
    val origConfig = ConfigFactory.load()
    val overrideConfigs: List[Config] = kryoConfig :: List().flatten

    val config: Config = overrideConfigs.
      reduce(_.withFallback(_)).
      withFallback(origConfig)

    val systemName = "ChatApp"
    val system1 = ActorSystem(systemName, config)
    val joinAddress = Cluster(system1).selfAddress
    Cluster(system1).join(joinAddress)
    system1.actorOf(Props[MemberListener], "memberListener")
    system1.actorOf(Props[RandomUser], "Ben")
    system1.actorOf(Props[RandomUser], "Kathy")

    Thread.sleep(500)
    val system2 = ActorSystem(systemName, config)
    Cluster(system2).join(joinAddress)
    system2.actorOf(Props[RandomUser], "Skye")

    Thread.sleep(500)
    val system3 = ActorSystem(systemName, config)
    Cluster(system3).join(joinAddress)
    system3.actorOf(Props[RandomUser], "Miguel")
    system3.actorOf(Props[RandomUser], "Tyler")
  }
}
