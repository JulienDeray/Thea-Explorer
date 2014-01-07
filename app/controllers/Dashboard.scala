package controllers

import play.api.mvc._
import core.Tools
import java.io.{FileInputStream, File}
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


object Dashboard extends Controller with Secured {

  val confForm = Form(
    "rootPath" -> nonEmptyText
  )

  val launchInitForm = Form(
    "status" -> nonEmptyText
  )

  def dashboard = IsAuthenticatedAsync { user => _ =>
    val futurFileSystem = scala.concurrent.Future { Tools.startBuildFileSystem( Application.config("root-folder") ) }
    futurFileSystem.map(
      fileSystem => Ok( views.html.index( fileSystem ) )
    )
  }

  def splash = IsAuthenticated { user => _ =>
    Ok( views.html.splash() )
  }

  def download(path: String) = Action {
    Ok.sendFile( new File( Application.config("root-folder") + "/" + Tools.unFormatFileUrl( path ) ) )
  }

  def downloadZip(path: String) = Action {
    import play.api.libs.iteratee._
    import java.util.zip._

    val formatedPath = Tools.unFormatFolderUrl( path )
    val enumerator = Enumerator.outputStream { os =>
      val rootPath = Application.config("root-folder") + "/"
      val zip = new ZipOutputStream( os )
      val filelist = Tools.listFiles( rootPath + formatedPath )
      val b = Array.fill[Byte](1024)(0)

    /*
    \\ pour Windows
    télécharger un sous dossier (exemple sur le --- pour les fichiers)
     */
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

    val contentDisposition = "attachment; filename=" +  formatedPath.substring(formatedPath.lastIndexOf("/") + 1print) + ".zip"
    Ok.chunked(enumerator >>> Enumerator.eof).withHeaders(
      "Content-Type" -> "application/zip",
      "Content-Disposition" -> contentDisposition
    )
  }

  def setConf() = Action { implicit request =>
    confForm.bindFromRequest().fold(
    formWithErrors => BadRequest( "You have to post a 'root' value" ),
    { root =>
      Application.config.saveProperties( Application.config.addProperty("root-folder", root) )
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
