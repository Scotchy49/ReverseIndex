package ReverseIndex

import io.Source
import java.io._
import java.nio.charset.{CodingErrorAction, Charset, CharsetDecoder}
import java.nio.channels.FileChannel

object App {
  val index = new ReverseIndex

  def readFile(f: File) : String = {
    val stream = new FileInputStream(f)
    try {
      val fc = stream.getChannel
      val bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())

      return Charset.defaultCharset().decode(bb).toString
    } finally {
      stream.close()
    }
  }

  def loadDocuments(file: File) {
    for (f <- file.listFiles) {
      if( f.isDirectory ) {
        loadDocuments(f)
      } else {
        println(f)
        index addDocument(f.getAbsolutePath, readFile(f))
      }
    }
  }

  def main( args: Array[String] ) {
    //args foreach { arg => loadDocuments(new File(arg)) }

    loadDocuments(new File("corpus"))
    //println(index.getDistribution())

    for ( ln <- Source.stdin.getLines ) {
      val start = System.currentTimeMillis()
      val docs = index find ln
      val duration = System.currentTimeMillis() - start
      println(
        <response>
          <time>{duration}</time>
          <docs>{
            docs.take(10).map{case (doc, score) =>
              <doc>
                <id>{doc}</id>
                <score>{score}</score>
              </doc>
                }
          }</docs>
        </response>
        )
      }
    }
}
