package controllers

import javax.inject.Inject

import dao.ShortUrlDao
import models.ShortUrl
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future
import scala.util.Try

class Application @Inject()(shortUrlDao: ShortUrlDao) extends Controller {

  def clear() = Action.async {
    shortUrlDao.clearAll().map { count =>
      Ok(Json.obj("result" -> count))
    }
  }

  def allShortUrls() = Action.async {
    shortUrlDao.all().map { all =>
      Ok(Json.toJson(all))
    }
  }

  def redirect(hash: String) = Action.async {
    shortUrlDao.findByHash(hash).map {
      case None => NotFound("Not Found, Sorry")
      case Some(url) => Redirect(url.fullUrl, 301)
    }
  }

  def shorten() = Action.async { request =>
    request.body.asJson.flatMap(js =>
      Try((js \ "fullUrl").as[String]).toOption
    ) match {
      case None => Future.successful(BadRequest("Request wasn't JSON with fullUrl"))
      case Some(fullUrl) =>
        ShortUrl.shorten(fullUrl).flatMap { shortUrl =>
          shortUrlDao.save(shortUrl) map { _ =>
            Ok(Json.obj("result" -> shortUrl))
          }
        } recover {
          case exeption => ServiceUnavailable("Sorry, Shorten Service unavailable")
        }
    }
  }

}
