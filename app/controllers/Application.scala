package controllers

import play.api.mvc._
import core.Tools
import java.io.{FileInputStream, File}
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

  def downloadZip(path: String) = Action {
    import play.api.libs.iteratee._
    import java.util.zip._

    val enumerator = Enumerator.outputStream { os =>
      val rootPath = config("root-folder") + "/"
      val zip = new ZipOutputStream( os )
      val filelist = Tools.listFiles( rootPath + path )
      val b = Array.fill[Byte](1024)(0)

      for ( filePath <- filelist ) {
        var count: Int = 1
        val in = new FileInputStream( rootPath + Tools.unFormatFileUrl( filePath ) )

        zip.putNextEntry( new ZipEntry( Tools.unFormatFileUrl( filePath ) ) )

        while ( count > 0 ) {
          zip.write(b, 0, count)
          count = in.read(b)
        }

        zip.closeEntry()
      }

      zip.close()
    }
    Ok.stream(enumerator >>> Enumerator.eof).withHeaders(
      "Content-Type" -> "application/zip",
      "Content-Disposition" -> "attachment; filename=test.zip"
    )
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