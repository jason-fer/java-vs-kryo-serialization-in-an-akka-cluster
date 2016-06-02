package chat

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object KryoSerializerConfig {
  val serializationClasses: Map[String, List[Class[_]]] = Map(
    "akkaKryo" -> List(
      classOf[java.lang.String],
      classOf[java.lang.Boolean],
      classOf[chat.ChatClient],
      scala.concurrent.duration.FiniteDuration.getClass,
      chat.Main.getClass,
      chat.KryoSerializerConfig.getClass,
      chat.ChatClient.getClass,
      chat.RandomUser.getClass,
      chat.RandomUser.Tick.getClass,
      chat.RandomUser.Tock.getClass,
      chat.ChatClient.Publish.getClass,
      chat.ChatClient.Message.getClass
    ))

  val akkaActorSerializationConfig: Config = {
    val mappings = serializationClasses.flatMap {
      case (serialization, classes) => classes.map { `class` => ('"' + `class`.getName + '"') -> serialization }
    }
    println(mappings)
    ConfigFactory.parseMap(
      Map(
        "akka.actor" -> Map(
          "serialization-bindings" -> mappings.asJava,
          "serializers" -> Map(
            "akkaKryo" -> "com.twitter.chill.akka.AkkaSerializer"
          ).asJava
        ).asJava
      ).asJava)
  }
}

