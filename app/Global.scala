
import model.user.{UserRepository, User}
import play.api._
/**
 * Created with IntelliJ IDEA.
 * User: math.herbert
 * Date: 07/01/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
object Global extends GlobalSettings{
  override def onStart(app: Application) {
    Logger.info("Application has started")
    val user : User = new User("root","root")
    UserRepository.addUser(user)
  }
}
