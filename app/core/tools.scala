package core

import model.{ServerFile, ServerEntity, ServerFolder}
import java.io.File

object tools {

  def systemFileBuilder(root: String): ServerFolder = {
    val path = new File(root)
    var content = List[ServerEntity]()

    for ( file <- path.listFiles() ) {
      if ( file.isDirectory )
        content = systemFileBuilder(file.getAbsolutePath) :: content
      else
        content = new ServerFile(file.getName, file.length(), file.getAbsolutePath) :: content
    }

    new ServerFolder(path.getName, content.reverse, root)
  }
}
