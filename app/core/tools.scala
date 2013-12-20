package core

import model.{ServerFile, ServerEntity, ServerFolder}
import java.io.File
import controllers.Pusher

object Tools {

  def refreshCounter(i: Double, total: Double) {
    Pusher.pushProgressBar( ((i*100) / total).toInt + "%")
  }

  def startBuildFileSystem(root: String): ServerFolder = {
    val path = new File(root)
    var content = List[ServerEntity]()
    val contentSize = path.listFiles().size
    var i: Double = 0

    for ( file <- path.listFiles() ) {
      if ( file.isDirectory )
        content = buildFileSystem(file.getAbsolutePath) :: content
      else
        content = new ServerFile(file.getName, file.length(), formatFileUrl( file.getAbsolutePath ) ) :: content
      i += 1
      refreshCounter(i, contentSize)
    }

    new ServerFolder(path.getName, content.reverse, root.substring(controllers.Application.config("root-folder").size))
  }
  
  def buildFileSystem(root: String): ServerFolder = {
    val path = new File(root)
    var content = List[ServerEntity]()

    for ( file <- path.listFiles() ) {
      if ( file.isDirectory )
        content = buildFileSystem(file.getAbsolutePath) :: content
      else
        content = new ServerFile(file.getName, file.length(), formatFileUrl( file.getAbsolutePath ) ) :: content
    }

    new ServerFolder(path.getName, content.reverse, root.substring(controllers.Application.config("root-folder").size))
  }

  def formatFileUrl(path: String): String = path.substring(controllers.Application.config("root-folder").size + 1).replaceAll("/", "---")
  def unFormatFileUrl(path: String): String = path.replaceAll("---", "/").replaceAll("%20", "\\ ")
}
