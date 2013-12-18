package model

class ServerFolder(val name: String, val content: List[ServerEntity]) extends ServerEntity {

  val icon: String = "glyphicon-folder-close"
  val size: Int = sizeOf(content)

  def sizeOf(theContent: List[ServerEntity]): Int = theContent match {
    case (x::xs) => x.size + sizeOf( xs )
    case Nil => 0
  }

}
