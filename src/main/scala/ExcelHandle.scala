import org.apache.poi.xssf.usermodel.{ XSSFWorkbook, XSSFSheet}
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Cell._

import java.io.{ File, FileInputStream }

import scala.collection.mutable.ListBuffer

object ExcelHandle {

  def read(fileName: String): Inventario = {

    val file = new FileInputStream(new File(fileName))

    //Create Workbook instance holding reference to .xlsx file
    val workbook = new XSSFWorkbook(file)

    //Get first/desired sheet from the workbook
    val sheet = workbook.getSheetAt(0)

    //mutable lists
    val countedUnits: ListBuffer[Option[Unit]] = ListBuffer()
    val stockUnits: ListBuffer[Option[Unit]] = ListBuffer()

    //Iterate through each rows one by one
    val rowIterator = sheet.iterator()
    rowIterator.next //don't want the first one, it's just the title

    while (rowIterator.hasNext) {

      val row = rowIterator.next

      val countedRef = readStringCell(row.getCell(0))
      val countedUnit = readIntCell(row.getCell(1))
      countedUnits.append(toOpUnit(countedRef, countedUnit))

      val stockRef = readStringCell(row.getCell(2)).map(s => s.split(" - ")(0))
      val stockUnit = readIntCell(row.getCell(3))
      stockUnits.append(toOpUnit(stockRef, stockUnit))

    }

    file.close()

    Inventario(countedUnits.toSet.flatten, stockUnits.toSet.flatten)
  }

  private def readStringCell(cell: Cell): Option[String] = Option(cell).map(c => c.getStringCellValue)
  private def readIntCell(cell: Cell): Option[Int] = Option(cell).map(c => c.getNumericCellValue.toInt)

  private def toOpUnit(ref: Option[String], num: Option[Int]): Option[Unit] =
    (ref, num) match {
      case (Some(r), Some(n)) => Some(Unit(r, n))
      case _ => None
    }

 }

