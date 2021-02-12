package lectures.part03Concurrency

import scala.concurrent.Future
import scala.util.{Failure, Success}

//important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesAndPromisses extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife //calculate the meaning of life on ANOTHER thread
  } //(global) which is passed by the compiler

  println(aFuture.value) //Option[Try[Int]]

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaninOfLife) => println(s"the meaning of life is $meaninOfLife")
    case Failure(ex) => println(s"I have failed with $ex")
  } //SOME thread

  Thread.sleep(3000)

}
