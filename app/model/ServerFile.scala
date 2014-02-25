package model

class ServerFile(val name: String, val size: Long, val path: String) extends ServerEntity {

  val content: List[ServerEntity] = Nil

  val fileType: String = {

    extention match {
      case "jpg" => "image"
      case "jpeg" => "image"
      case "jpe" => "image"
      case "bmp" => "image"
      case "gif" => "image"
      case "png" => "image"
      case "mp3" => "music"
      case _ => "unknown"
    }
  }
}
