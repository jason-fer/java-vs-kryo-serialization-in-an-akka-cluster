package chat

import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.concurrent.duration._
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

object RandomUser {
  case object Tock
  case object Tick
  val phrases = Vector(
    "Creativity is allowing yourself to make mistakes. Art is knowing which ones to keep.",
    "The best way to compile inaccurate information that no one wants is to make it up.",
    "Decisions are made by people who have time, not people who have talent.",
    "Frankly, I'm suspicious of anyone who has a strong opinion on a complicated issue.",
    "Nothing inspires forgiveness quite like revenge.",
    "Free will is an illusion. People always choose the perceived path of greatest pleasure.",
    "The best things in life are silly.",
    "Remind people that profit is the difference between revenue and expense. This makes you look smart.",
    "Engineers like to solve problems. If there are no problems handily available, they will create their own problems.",
    "Whether you think you can or think you can’t, you’re right.",
    "Dreams don’t work unless you do.",
    "Act as if what you do makes a difference. It does.\t",
    "Man who run in front of car get tired.",
    "Man who run behind car get exhausted.",
    "Man with one chopstick go hungry.",
    "Man who eat many prunes get good run for money.",
    "War does not determine who is right, war determine who is left.",
    "Man who fight with wife all day get no piece at night.",
    "Man who stand on toilet is high on pot.",
    "Man who drive like hell, bound to get there.",
    "Man who live in glass house should change clothes in basement.",
    "Man who fart in church sit in own pew.",
    "Crowded elevator smell different to midget.",
    "Man who sit on tack get point.")
}

class RandomUser extends Actor {
  import RandomUser._
  import context.dispatcher
  val client = context.actorOf(ChatClient.props(self.path.name), "client")

  def scheduler = context.system.scheduler
  def rnd = ThreadLocalRandom.current

  var shouldStop = false

  override def preStart(): Unit =
    scheduler.scheduleOnce(10.milliseconds, self, Tick)
    scheduler.scheduleOnce(10.seconds, self, Tock)

  // override postRestart so we don't call preStart and schedule a new Tick
  override def postRestart(reason: Throwable): Unit = ()

  var count = 1

  def receive = {
    case Tick =>
//      scheduler.scheduleOnce(rnd.nextInt(5, 10).milliseconds, self, Tick)
      if(! shouldStop){
        scheduler.scheduleOnce(0.milliseconds, self, Tick)
        val msg = phrases(rnd.nextInt(phrases.size))
        count += 1
        client ! ChatClient.Publish(msg)
      }

    case Tock => {
      println("xxxxxxxxxxxxxxxxxxxxxxxx ----------------------------------------- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
      println(s"xxxxxxxxxxxxxxxxxxxxxxxx !!!!!!!!!    ${self.path.name} should stop     !!!!!!!!!!!! xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
      println("xxxxxxxxxxxxxxxxxxxxxxxx ----------------------------------------- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
      shouldStop = true
    }
  }

}