import akka.actor.{Actor, Props, ActorSystem}
import akka.dispatch.ExecutionContext
import akka.util.Timeout
import org.scalatra.akka.AkkaSupport
import org.scalatra.{Accepted, ScalatraServlet}

class MyActorApp extends ScalatraServlet with AkkaSupport {

  import _root_.akka.pattern.ask
  protected implicit def executor: ExecutionContext = system.dispatcher

  val system = ActorSystem()
  implicit val timeout = Timeout(10)

  val myActor = system.actorOf(Props[MyActor])

  get("/async") {
    myActor ? "Do stuff and give me an answer"
  }

  get("/fire-forget") {
    myActor ! "Hey, you know what?"
    Accepted()
  }

}

class MyActor extends Actor {
  def receive = {
    case "Do stuff and give me an answer" => sender ! "The answer is 42"
    case "Hey, you know what?" => println("Yeah I know... oh boy do I know")
  }

}
