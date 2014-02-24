package controllers

import play.api.mvc._
import core.Tools
import java.io.{FileInputStream, File}
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.Play
import play.api.Play.current
import model.{ServerRootFolder, ServerEntity}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

object Dashboard extends Controller with Secured {

  val rootFolder = Application.config("root-folder")

  val confForm = Form(
    "rootPath" -> nonEmptyText
  )

  val launchInitForm = Form(
    "status" -> nonEmptyText
  )

  val nextPageForm = Form(
    "noPage" -> nonEmptyText
  )

  lazy val rootEntity: ServerRootFolder = {
    val rootFile = new File( rootFolder )
    Tools.startBuildFileSystem( rootFile )
  }

  def dashboard = IsAuthenticatedAsync { user => _ =>
    val rootFile = new File( rootFolder )
    if ( rootFile.exists() ) {
      scala.concurrent.Future { Ok( views.html.index( rootEntity ) ) }
    }
    else {
      scala.concurrent.Future { Ok( views.html.pathError() ) }
    }
  }

  def splash = IsAuthenticated { user => _ =>
    Ok( views.html.splash() )
  }

  def download(path: String) = Action {
    Ok.sendFile( new File( rootFolder + "/" + Tools.unFormatFileUrl( path ) ) )
  }

  def downloadZip(path: String) = Action {
    import play.api.libs.iteratee._
    import java.util.zip._

    val formatedPath = Tools.unFormatFolderUrl( path )
    val enumerator = Enumerator.outputStream { os =>
      val rootPath = rootFolder + "/"
      val zip = new ZipOutputStream( os )
      val filelist = Tools.listFiles( rootPath + formatedPath )
      val b = Array.fill[Byte](1024)(0)

      for ( filePath <- filelist ) {
        var count: Int = 0
        val in = new FileInputStream( rootPath + Tools.unFormatFileUrl( filePath ) )

        zip.putNextEntry( new ZipEntry( Tools.unFormatFileUrl( filePath ) ) )

        do {
          zip.write(b, 0, count)
          count = in.read(b, 0, 1024)
        } while ( count > 0 )

        zip.closeEntry()
      }

      zip.close()
    }

    val contentDisposition = "attachment; filename=" +  formatedPath.substring(formatedPath.lastIndexOf("/") + 1) + ".zip"
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

  def picViewer(path: String) = Action {
    val unformatedPath = Tools.unFormatFileUrl( path )
    Ok( views.html.modalPicViewer( rootFolder + "/" + unformatedPath, path ) )
  }

  def picGetter(path: String) = Action {
    val file = new File( "/" + path )
    val source = scala.io.Source.fromFile(file)(scala.io.Codec.ISO8859)
    val byteArray = source.map(_.toByte).toArray
    source.close()

    Ok(byteArray).as("image/jpeg")
  }

  def getNextPageNo() = Action { implicit request =>
    nextPageForm.bindFromRequest().fold(
      formWithErrors => BadRequest( "Serious issue !" ),
      { noPage => {
          Pusher.pushPageNumber( noPage.toInt )
          Ok
        }
      }
    )
  }

  def getPage(noPage: String) = Action {
    Ok( views.html.lines( rootEntity.pagedContent( noPage.toInt ) ) )
  }
}
