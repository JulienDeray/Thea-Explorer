package controllers

import play.api.mvc._
import core.Tools
import java.io.File
import play.api.data.Form
import play.api.data.Forms._
import core.ConfigManager

object Application extends Controller {

  val config = new ConfigManager

  val confForm = Form(
    "rootPath" -> nonEmptyText
  )

  def index = Action {
    Ok( views.html.index( Tools.systemFileBuilder(config("root-folder") ) ) )
  }

  def download(path: String) = Action {
    Ok.sendFile( new File(config("root-folder") + "/" + Tools.unFormatFileUrl( path ) ) )
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

  def interpretedJS() = Action { implicit request =>
    Ok( views.html.interpretedJS() )
  }

}