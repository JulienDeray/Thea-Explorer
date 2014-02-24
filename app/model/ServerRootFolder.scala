package model

class ServerRootFolder( override val name: String, override val content: List[ServerEntity], override val path: String ) extends ServerFolder(name, content, path) {

  lazy val pagedContent: List[List[ServerEntity]] = {
    var i = 0
    var res = List[List[ServerEntity]]()
    var tempContent = List[ServerEntity]()
    content.map { entity =>
      if ( i % 30 == 0 && i != 0 ) {
        res = tempContent :: res
        tempContent = List[ServerEntity]()
      }

      tempContent = entity :: tempContent
      i += 1
    }
    res.reverse
  }
}
