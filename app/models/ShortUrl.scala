package models

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.Future

case class ShortUrl(hash: String, fullUrl: String)

object ShortUrl {
  implicit val format = Json.format[ShortUrl]
  private val hashProvider = WS.url("https://www.random.org/strings/?num=1&len=10&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new")

  def shorten(fullUrl: String): Future[ShortUrl] = {
    generateHashId(fullUrl).flatMap {
      case Some(hash) => Future.successful(ShortUrl(hash, fullUrl))
      case None => Future.failed(new RuntimeException("Service Unavailable"))
    }
  }

  private def generateHashId(fullUrl: String): Future[Option[String]] = {
    hashProvider.withRequestTimeout(1000).get().map { response =>
      response.status match {
        case 200 => Some(response.body.trim)
        case _ => None
      }
    } recover {
      case exception => None
    }
  }
}

