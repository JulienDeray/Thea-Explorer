package model

trait ServerEntity {

  val name: String
  val size: Long
  val icon: String
  val path: String
  val content: List[ServerEntity]

  def formatedSize: (Long, String) = {
    size match {
      case _ if size < 1000       => (size, " b")
      case _ if size < 1000000    => (size/1000, " Kb")
      case _ if size < 1000000000 => (size/1000000, " Mb")
      case _                      => (size/1000000000, " Gb")
    }
  }
}
