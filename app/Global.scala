
import model.user.{UserRepository, User}
import play.api._

object Global extends GlobalSettings{

  override def onStart(app: Application) {
    Logger.info("Application has started")

    val user : User = new User("root","root")
    UserRepository.addUser(user)
  }
}
