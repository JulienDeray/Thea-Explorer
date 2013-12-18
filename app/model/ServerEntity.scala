package model

trait ServerEntity {

  val name: String
  val size: Long
  val icon: String
  val content: List[ServerEntity]
}
