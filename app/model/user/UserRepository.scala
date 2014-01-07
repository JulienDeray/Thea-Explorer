package model.user

object UserRepository {

    private var users : List[User] = Nil

    def addUser( user : User ){
      users  = users . :: (user)
    }

    def getUser( nom : String, password : String ): User = {
      for ( u <- users if u.login == nom && u.password == password ) return u
      null
    }

}
