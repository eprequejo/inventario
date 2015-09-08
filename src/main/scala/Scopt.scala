case class ConfigScopt(filePath: Option[String] = None)

object Scopt {

  def getArgs = {
    new scopt.OptionParser[ConfigScopt]("inventario") {
      head("scopt", "3.x")
      opt[String]('f', "file") action { (x, c) => c.copy(filePath = Option(x)) } text("Path to the input file to process")
      help("help") text("Prints this usage text")
    }
  }

}


