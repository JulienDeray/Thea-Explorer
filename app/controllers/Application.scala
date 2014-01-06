package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import core.ConfigManager
import scala.concurrent.Future

object Application extends Controller {

  val config = new ConfigManager

  val loginForm = Form(
    tuple(
      "user" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def login = Action { implicit request =>
    request.session.get("user").map(
      user => Redirect( "/splash" )
    ).getOrElse( Ok( views.html.login() ) )
  }

  def logout = Action {
    Redirect( "/" ).withNewSession
  }

  def authentification = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Ok( views.html.login() )
      },
      user => {
        if ( user._1 == config("user") && user._2 == config("password") )
          Redirect( "/splash" ).withSession( "user" -> user._1 )
        else {
          Ok( views.html.login() )
        }
      }
    )
  }

}


/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user name.
   */
  private def user( request: RequestHeader ) = request.session.get("user")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized( request: RequestHeader ) = Results.Redirect( routes.Application.login( ) )

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated( f: => String => Request[AnyContent] => Result ) = Security.Authenticated( user, onUnauthorized ) { user =>
    Action( request => f( user )( request ) )
  }

  def IsAuthenticatedAsync( f: => String => Request[AnyContent] => Future[SimpleResult] ) = Security.Authenticated( user, onUnauthorized ) { user =>
    Action.async( request => f( user )( request ) )
  }

}