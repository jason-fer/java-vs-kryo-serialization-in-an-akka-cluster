package chat

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object KryoSerializerConfig {
  val serializationClasses: Map[String, List[Class[_]]] = Map(
    "akkaKryo" -> List(
      classOf[ChatClient],
      classOf[MemberListener],
      classOf[RandomUser],
      ChatClient.getClass,
      Main.getClass,
      chat.RandomUser.getClass,
      KryoSerializerConfig.getClass
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

