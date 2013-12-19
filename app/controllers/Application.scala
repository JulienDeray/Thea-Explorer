package controllers

import play.api.mvc._
import com.typesafe.config.ConfigFactory
import core.tools

object Application extends Controller {

  val root = ConfigFactory.load("customParameters.properties").getString("root-folder")

  def index = Action {
    Ok( views.html.index( tools.systemFileBuilder(root) ) )
  }

}