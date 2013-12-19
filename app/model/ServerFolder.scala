package model

class ServerFolder(val name: String, val content: List[ServerEntity], val path: String) extends ServerEntity {

  val icon: String = "glyphicon-folder-close"
  val size: Long = sizeOf(content)

  def sizeOf(theContent: List[ServerEntity]): Long = theContent match {
    case (x::xs) => x.size + sizeOf( xs )
    case Nil => 0
  }

}
