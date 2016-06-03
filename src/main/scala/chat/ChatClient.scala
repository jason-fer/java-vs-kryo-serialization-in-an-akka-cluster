package chat

import akka.actor.{Actor, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe}

object ChatClient {
  def props(name: String): Props = Props(classOf[ChatClient], name)

  case class Publish(msg: String) extends Serializable
  case class Message(from: String, text: String) extends Serializable
}

class ChatClient(name: String) extends Actor {
  val mediator = DistributedPubSub(context.system).mediator
  val topic = "chatroom"
  mediator ! Subscribe(topic, self)
  println(s"$name joined chat room")

  var count = 1
  def receive = {
    case ChatClient.Publish(msg) =>
      count += 1
//      println(s"${name} publish (${count})")
      mediator ! Publish(topic, ChatClient.Message(name, msg))

    case ChatClient.Message(from, text) =>
      count += 1
      val direction = if (sender == self) ">>>>" else s"<< $from:"

      if((count % 1000) == 0)
//        println(s"${name} $direction (${count}) $text")
        println(s"${name} (${count})")

  }

}