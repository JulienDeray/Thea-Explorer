package model

class ServerFile(val name: String, val size: Int) extends ServerEntity {

  val icon: String = "glyphicon-file"
  val content: List[ServerEntity] = Nil
}
