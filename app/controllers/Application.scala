package controllers

import play.api.mvc._
import core.Tools
import java.io.File
import play.api.data.Form
import play.api.data.Forms._
import core.ConfigManager
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Application extends Controller {

  val config = new ConfigManager

  val confForm = Form(
    "rootPath" -> nonEmptyText
  )

  val launchInitForm = Form(
    "status" -> nonEmptyText
  )

  def index = Action.async {
    val futurFileSystem = scala.concurrent.Future { Tools.startBuildFileSystem( config("root-folder") ) }
    futurFileSystem.map(
      fileSystem => Ok( views.html.index( fileSystem ) )
    )
  }

  def splash = Action {
    Ok( views.html.splash() )
  }

  def download(path: String) = Action {
    Ok.sendFile( new File( config("root-folder") + "/" + Tools.unFormatFileUrl( path ) ) )
  }

  def setConf() = Action { implicit request =>
    confForm.bindFromRequest().fold(
      formWithErrors => BadRequest( "You have to post a 'root' value" ),
      { root =>
        config.saveProperties( config.addProperty("root-folder", root) )
        Ok
      }
    )
  }

  def launchInit() = Action { implicit request =>
    launchInitForm.bindFromRequest().fold(
      formWithErrors => BadRequest( "Serious issue !" ),
      { status =>

        Ok
      }
    )
  }

  def interpretedJS() = Action { implicit request =>
    Ok( views.html.interpretedJS() )
  }

  def webSocketJS() = Action { implicit request =>
    Ok( views.html.webSocket() )
  }

}