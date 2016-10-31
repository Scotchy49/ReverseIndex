package ReverseIndex

import org.junit._
import Assert._

@Test
class AppTest {
  @Test
  def tokenizeTest() = {
    val input = "The quick brown fox, jumps on the cat."
    val tokens = Tokenizer.tokenize(input)
    val expected = Map("the" -> 2, "quick" -> 1, "brown" -> 1, "fox" -> 1, "jumps" -> 1, "on" -> 1, "cat" -> 1)
    assertEquals(expected, tokens);
  }

  @Test
  def documentAdd() = {
    val index = new ReverseIndex
    val input = "input text"
    index addDocument("1", input)

    assertTrue(index.storeSize == 1)
    assertTrue(index.indexSize == 2)
  }

  @Test
  def documentFind() = {
    val index = new ReverseIndex
    val input = "the quick brown fox"
    val input2 = "jumps on the cat"
    index addDocument("1", input)
    index addDocument("2", input2)

    val r1 = index find "brown fox"
    val r2 = index find "jumps cat"

    println( index find "the cat" )
  }

  @Test
  def fail() = {
    assertTrue(true == false)
  }
}


