package model

trait ServerEntity {

  val name: String
  val size: Long
  val path: String
  val content: List[ServerEntity]
  val fileType: String

  val extention: String = try {
    val ext = name.substring( name.lastIndexOf('.') + 1 )
    ext.toLowerCase
  } catch {
    case e: Exception => "Unknown"
  }


  def formatedSize: (Long, String) = size match {
    case _ if size < 1000       => (size, " b")
    case _ if size < 1000000    => (size/1000, " Kb")
    case _ if size < 1000000000 => (size/1000000, " Mb")
    case _                      => (size/1000000000, " Gb")
  }

}
