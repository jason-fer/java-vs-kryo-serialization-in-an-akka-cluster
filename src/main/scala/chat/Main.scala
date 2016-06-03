package chat

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.{Config, ConfigFactory}

object Main {

  val blah: Any = "xxx"
  def main(args: Array[String]): Unit = {

    val kryoConfig: List[Config] = List(KryoSerializerConfig.akkaActorSerializationConfig)
    val origConfig = ConfigFactory.load()

    val config: Config = kryoConfig.
      reduce(_.withFallback(_)).
      withFallback(origConfig)

//    val config = ConfigFactory.load()
//    println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
//    println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
//    println(config)
//    println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
//    println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")


    val systemName = "ChatApp"
    val system1 = ActorSystem(systemName, config)

    println(system1.logConfiguration())

    val joinAddress = Cluster(system1).selfAddress
    Cluster(system1).join(joinAddress)
    system1.actorOf(Props[MemberListener], "memberListener")
    system1.actorOf(Props[RandomUser], "Ben")
    system1.actorOf(Props[RandomUser], "Kathy")

    Thread.sleep(10)
    val system2 = ActorSystem(systemName, config)
    Cluster(system2).join(joinAddress)
    system2.actorOf(Props[RandomUser], "Skye")

    Thread.sleep(10)
    val system3 = ActorSystem(systemName, config)
    Cluster(system3).join(joinAddress)
    system3.actorOf(Props[RandomUser], "Miguel")
    system3.actorOf(Props[RandomUser], "Tyler")

    Thread.sleep(1000)
    System.exit(1)
  }
}
