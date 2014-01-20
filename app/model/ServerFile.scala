package model

class ServerFile(val name: String, val size: Long, val path: String) extends ServerEntity {

  val content: List[ServerEntity] = Nil

  val fileType: String = {
    if ( extention == "jpg" ||
      extention == "jpeg" ||
      extention == "jpe" ||
      extention == "bmp" ||
      extention == "gif" ||
      extention == "png" )
      "image"
    else
      "unknown"
  }
}
