package ReverseIndex

import java.util.StringTokenizer
import collection.immutable.HashMap

/**
 * User: scotchy
 * Date: 14/07/12
 * Time: 14:01
 */

object Tokenizer {
  def tokenize( text: String ) : Map[String,Int] = {
    val tokens = text.split("([.,!?:;'\"-]|\\s)+")
      .map{s:String => s.toLowerCase}

    var dist = Map[String,Int]()
    tokens foreach {t: String => {
        val c = dist.getOrElse(t, 0)
        dist += (t -> (c + 1))
      }
    }
    dist
  }
}
