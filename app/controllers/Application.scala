package controllers

import play.api.mvc._
import com.typesafe.config.ConfigFactory
import core.Tools

object Application extends Controller {

  val root = ConfigFactory.load("customParameters.properties").getString("root-folder")

  def index = Action {
    Ok( views.html.index( Tools.systemFileBuilder(root) ) )
  }

}