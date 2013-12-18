package controllers

import play.api.mvc._
import model._
import com.typesafe.config.ConfigFactory
import java.io.File

object Application extends Controller {

  val root = ConfigFactory.load("customParameters.properties").getString("root-folder")

  val testValues: List[ServerEntity] = List(
    new ServerFolder("Dir1", List(
      new ServerFolder("Dir2", List(
        new ServerFile("Fil4", 10)
      )),
      new ServerFile("Fil1", 10),
      new ServerFile("Fil2", 10),
      new ServerFile("Fil3", 10)
    )),
    new ServerFile("Fil5", 30)
  )

  def systemFileBuilder(root: String): ServerFolder = {
    val path = new File(root)
    var content = List[ServerEntity]()

    for ( file <- path.listFiles() ) {
      if ( file.isDirectory )
        content = systemFileBuilder(file.getAbsolutePath) :: content
      else
        content = new ServerFile(file.getName, file.getTotalSpace) :: content
    }

    new ServerFolder(path.getName, content)
  }

  def index = Action {
    Ok( views.html.index( systemFileBuilder(root) ) )
  }

}