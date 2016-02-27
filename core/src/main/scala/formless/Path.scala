package formless

sealed trait Path {
  def / (field: String): Path = PField(this, field)
  def / (index: Int   ): Path = PIndex(this, index)
}

final case object PNil extends Path {
  override def toString = "/"
}

final case class PField(root: Path, field: String) extends Path {
  override def toString = s"${root}${field}/"
}

final case class PIndex(root: Path, index: Int) extends Path {
  override def toString = s"${root}${index}/"
}
