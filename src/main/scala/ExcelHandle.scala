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
    val countedUnits: ListBuffer[Unit] = ListBuffer()
    val stockUnits: ListBuffer[Unit] = ListBuffer()

    //Iterate through each rows one by one
    val rowIterator = sheet.iterator()
    rowIterator.next //don't want the first one, it's just the title

    while (rowIterator.hasNext) {

      val row = rowIterator.next

      val countedRef = row.getCell(0).getStringCellValue
      val countedUnit = row.getCell(1).getNumericCellValue.toInt
      countedUnits.append(Unit(countedRef, countedUnit))

      val stockRef = row.getCell(2).getStringCellValue
      //formatting
      val stockRefFormatted = stockRef.split(" -")
      val stockUnit = row.getCell(3).getNumericCellValue.toInt
      stockUnits.append(Unit(stockRefFormatted(0), stockUnit))

    }

    file.close()

    Inventario(countedUnits.toSet, stockUnits.toSet)
  }
}

