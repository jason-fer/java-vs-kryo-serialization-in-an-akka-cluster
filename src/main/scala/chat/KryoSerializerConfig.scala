package chat

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object KryoSerializerConfig {
  val serializationClasses: Map[String, List[Class[_]]] = Map(
    "akkaKryo" -> List(
//      classOf[java.lang.String],
//      classOf[java.lang.Boolean],
//      classOf[chat.ChatClient],
      classOf[java.io.Serializable]
//      scala.concurrent.duration.FiniteDuration.getClass,
//      chat.Main.getClass,
//      chat.KryoSerializerConfig.getClass,
//      chat.ChatClient.getClass,
//      chat.RandomUser.getClass,
//      chat.RandomUser.Tick.getClass,
//      chat.RandomUser.Tock.getClass,
//      chat.ChatClient.Publish.getClass,
//      chat.ChatClient.Message.getClass
    ))

  //  val serializationClasses: Map[String, List[String]] = Map(
  //    "akkaKryo" -> List(
  //      "scala.concurrent.duration.FiniteDuration",
  //      "java.lang.String",
  //      "java.lang.Boolean",
  //      "chat.ChatClient",
  //      "chat.Main",
  //      "chat.KryoSerializerConfig",
  //      "chat.ChatClient",
  //      "chat.RandomUser",
  //      "chat.RandomUser$Tick$",
  //      "chat.RandomUser$Tock$",
  //      "chat.ChatClient$Publish",
  //      "chat.ChatClient$Message
  //    ))

  val akkaActorSerializationConfig: Config = {
    val mappings = serializationClasses.flatMap {
      case (serialization, classes) => classes.map { `class` => {
        println("-----------------------------------------")
        println(s"class -> ${`class`}")
        println(s"getClasses -> ${`class`.getClasses}")
        println(s"getDeclaringClass -> ${`class`.getDeclaringClass}")
        println(s"getDeclaredClasses -> ${`class`.getDeclaredClasses}")
        println(s"getEnclosingClass -> ${`class`.getEnclosingClass}")
        println(s"getCanonicalName -> ${`class`.getCanonicalName}")
        println(s"getComponentType -> ${`class`.getComponentType}")
//        if (Option(`class`.getEnclosingClass).isDefined) {
//          ('"' + `class`.getName + '"') -> serialization
//        } else {
//          ('"' + `class`.getName.stripSuffix("$") + '"') -> serialization
//        }
        ('"' + `class`.getName + '"') -> serialization
      }}
      //      case (serialization, classes) => classes.map { `class` => ('"' + `class` + '"') -> serialization }
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

