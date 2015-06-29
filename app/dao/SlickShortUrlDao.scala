package dao


import javax.inject.Inject

import models.ShortUrl
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future


class SlickShortUrlDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ShortUrlDao
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val ShortUrlsTable = TableQuery[ShortUrls]

  def clearAll(): Future[Int] = db.run(ShortUrlsTable.delete)

  def findByHash(hash: String): Future[Option[ShortUrl]] =
    db.run(ShortUrlsTable.filter(_.hash === hash).take(1).result.headOption)


  def all(): Future[Seq[ShortUrl]] = db.run(ShortUrlsTable.result)

  def save(url: ShortUrl): Future[Unit] = db.run(ShortUrlsTable += url).map { _ => () }

  private class ShortUrls(tag: Tag) extends Table[ShortUrl](tag, "Urls") {

    override def * = (hash, fullUrl) <>((ShortUrl.apply _).tupled, ShortUrl.unapply)

    def hash = column[String]("hashCol")

    def fullUrl = column[String]("fullUrl")

  }

}
