package dao

import models.ShortUrl

import scala.concurrent.Future

trait ShortUrlDao {

  def clearAll(): Future[Int]

  def findByHash(hash: String): Future[Option[ShortUrl]]

  def all(): Future[Seq[ShortUrl]]

  def save(url: ShortUrl): Future[Unit]
}
