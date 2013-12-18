package model

trait ServerEntity {

  val name: String
  val size: Int
  val icon: String
  val content: List[ServerEntity]
}
