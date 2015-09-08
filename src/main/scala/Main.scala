import scala.collection.mutable.ListBuffer


object Main extends App {

  val parser = Scopt.getArgs

  parser.parse(args, ConfigScopt()).flatMap { opts =>

    opts.filePath. map { f =>

      println("STARTING INVENTARIO =======================================")

      //read excel
      println("Reading excel file ...")
      val inventario = ExcelHandle.read(f)

      //get differences
      println("Calculating differences ...")
      val diffs: Set[InventarioRow] = calculateDiffs(inventario)
      println("DIFFERENCES ================================================")
      diffs.foreach(i => println(i.print))

      //write excel with diff rows - println for now

      //send email - not for now

      println("FINISHED INVENTARIO ========================================")
    }

  } getOrElse (println(parser.usage))

  private def calculateDiffs(inventario: Inventario): Set[InventarioRow] = {

    val diffCountedUnits = inventario.countedUnits -- inventario.stockUnits
    val diffStockUnits = inventario.stockUnits -- inventario.countedUnits

    val diff: ListBuffer[InventarioRow] = ListBuffer()

    diffCountedUnits.foreach{ cu =>
      val su = diffStockUnits.find(s => s.ref == cu.ref)
      su.map(s => diff.append(InventarioRow(cu.ref, cu.unitNum, s.unitNum))).getOrElse(diff.append(InventarioRow(cu.ref, cu.unitNum, 0)))
    }

    diffStockUnits.foreach{ su =>
      val cu = diffCountedUnits.find(s => s.ref == su.ref)
      cu.map(s => diff.append(InventarioRow(su.ref, s.unitNum, su.unitNum))).getOrElse(diff.append(InventarioRow(su.ref, 0, su.unitNum)))
    }

    diff.toSet
  }

}
