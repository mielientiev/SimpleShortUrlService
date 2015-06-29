package modules

import com.google.inject.AbstractModule
import dao.{SlickShortUrlDao, ShortUrlDao}


class DaoModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ShortUrlDao])
      .to(classOf[SlickShortUrlDao])
  }
}
