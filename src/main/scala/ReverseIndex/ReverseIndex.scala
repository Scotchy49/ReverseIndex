package ReverseIndex

import collection.immutable.{ListMap, HashMap}

/**
 * User: scotchy
 * Date: 14/07/12
 * Time: 14:18
 */

class ReverseIndex {
  var documentIndex = HashMap[String,Map[Int, Int]]()
  var documentStore = Array[String]()

  def indexDocument(i: Int, s: String) {
    val tokens = Tokenizer.tokenize(s)

    for((token,count) <- tokens)  {
      var documentList = documentIndex.getOrElse(token, Map[Int,Int]())
      documentList += (i -> count)
      documentIndex += (token -> (documentList))
    }
  }

  def addDocument(i: String, text: String) {
    documentStore = documentStore :+ i
    val id = documentStore.length - 1

    indexDocument(id, text)
  }

  def findTermDocs(term: String): Map[String,Int] = {
    documentIndex.getOrElse(term, Map[Int,Int]()).map{case(k,v) => documentStore(k) -> v}
  }

  def findDocFreq(term: String): Int = {
    documentIndex.getOrElse(term, Map[Int,Int]()).map(_._2).sum
  }

  def scoreDocuments(queryTerms: Map[String, Map[String, Int]]): ListMap[String, Double] = {
    // go from q -> (doc -> count)
    // to doc -> q -> count

    var documents = Map[String, Map[String, Int]]()

    for((queryTerm, docs) <- queryTerms) {
      docs foreach{ case(doc, count) =>
        val nDoc = documents.getOrElse(doc, Map[String,Int]()) + (queryTerm -> count)
        documents += (doc -> nDoc)
      }
    }

    // now score the documents
    ListMap(documents.map{case (doc, queryTerms) =>
      (doc -> queryTerms.map{case (term, tf) =>
        val df = findDocFreq(term)
        val idf = 1 + math.log(documentStore.size / df+1)
        tf * idf * idf
      }.sum)
    }.toList.sortBy(-_._2):_*)
  }

  def find(query: String): ListMap[String, Double] = {
    val q = Tokenizer.tokenize(query)

    // this gives q -> (doc -> count)
    val docs = q.map{case(token,c) =>
      val docs = findTermDocs(token.toLowerCase)
      (token -> docs)
    }

    scoreDocuments(docs)
  }

  def getDoc(id: Int): String = documentStore(id)

  def storeSize = documentStore.size
  def indexSize = documentIndex.size
}
