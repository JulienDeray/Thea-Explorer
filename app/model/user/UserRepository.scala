package model.user

/**
 * Created with IntelliJ IDEA.
 * User: math.herbert
 * Date: 07/01/14
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
object UserRepository {

    private var users : List[User]=Nil
    def addUser( user : User){
      users  = users.::(user)
    }
    def getUser(nom : String, password : String): User={
      for(u <- users if u.login==nom && u.password ==password) return u
      null
    }

}
