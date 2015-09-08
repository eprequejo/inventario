
case class Inventario(countedUnits: Set[Unit], stockUnits: Set[Unit])

case class Unit(ref: String, unitNum: Int)

case class InventarioRow(ref: String, countedUnit: Int, stockUnit: Int) {

  //not using it
  def toInventarioRow(counted: Option[Unit], stock: Option[Unit]): Option[InventarioRow] =
    (counted, stock) match {
      case (Some(cu), None) => Some(InventarioRow(cu.ref, cu.unitNum, 0))
      case (None, Some(su)) => Some(InventarioRow(su.ref, 0, su.unitNum))
      case (Some(cu), Some(su)) => Some(InventarioRow(cu.ref, cu.unitNum, su.unitNum))
      case _ => None
  }

  def print: String = {
    s"""Referencia: ${this.ref} - unidades contadas: ${this.countedUnit} - unidades en stock: ${this.stockUnit}"""
  }

}


