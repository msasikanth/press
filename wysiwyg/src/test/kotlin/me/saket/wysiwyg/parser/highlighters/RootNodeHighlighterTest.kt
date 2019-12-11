package me.saket.wysiwyg.parser.highlighters

import assertk.Assert
import assertk.assertThat
import assertk.assertions.support.fail
import me.saket.wysiwyg.parser.MarkdownParser
import me.saket.wysiwyg.style.WysiwygStyle
import kotlin.test.Test

class RootNodeHighlighterTest {

  private val style = wysiwygStyle()
  private val writer = FakeSpanWriter(style)
  private val parser = MarkdownParser()

  private fun parseAndVisit(bold: String) {
    val node = parser.parseSpans(bold)
    RootNodeHighlighter.visit(node, writer)
  }

  @Test fun bold() {
    val markdown = """
      |**It's** not who you are underneath.
      |It's **what** you do that **defines** you.
    """.trimMargin()
    parseAndVisit(markdown)

    assertThat(writer.highlights).isEqualTo(
        FakeSpannedBuilder(style)
            .bold("It's")
            .text(" not who you are\nunderneath, it's ")
            .bold("what")
            .text(" you\ndo that ")
            .bold("defines")
            .text(" you.")
            .build()
    )
  }

  @Test fun italics() {
    val markdown = """
      |Why do we *Fall?*
      |So we can *learn* to pick ourselves back up.
    """.trimMargin()
    parseAndVisit(markdown)

    assertThat(writer.highlights).isEqualTo(
        FakeSpannedBuilder(style)
            .text("Why do we ")
            .italics("Fall?")
            .text("\nSo we can ")
            .italics("learn")
            .text(" to pick ourselves back up.")
            .build()
    )
  }

  @Test fun strikethrough() {
    val markdown = """
      |How can two ~~people~~ hate so much
      |without ~~knowing~~ each other?
    """.trimMargin()
    parseAndVisit(markdown)

    assertThat(writer.highlights).isEqualTo(
        FakeSpannedBuilder(style)
            .text("How can two ")
            .strikethrough("people")
            .text(" hate so much\nwithout ")
            .strikethrough("knowing")
            .text(" each other?")
            .build()
    )
  }

  companion object {
    private fun wysiwygStyle(): WysiwygStyle {
      return WysiwygStyle(
          syntaxColor = 0xFFCCAEF9.toInt(),
          strikethroughTextColor = 0xFF9E9E9E.toInt(),
          blockQuote = WysiwygStyle.BlockQuote(
              leftBorderColor = 0xFF353846.toInt(),
              leftBorderWidth = 4,
              indentationMargin = 8,
              textColor = 0xFFCED2F8.toInt()
          ),
          code = WysiwygStyle.Code(
              backgroundColor = 0xFF242632.toInt(),
              codeBlockMargin = 12
          ),
          heading = WysiwygStyle.Heading(
              textColor = 0xFF85F589.toInt()
          ),
          link = WysiwygStyle.Link(
              titleTextColor = 0xFF8DF0FF.toInt(),
              urlTextColor = 0xFFAAC6D1FF.toInt()
          ),
          list = WysiwygStyle.List(
              indentationMargin = 16
          ),
          thematicBreak = WysiwygStyle.ThematicBreak(
              color = 0xFF62677C.toInt(),
              height = 20f
          )
      )
    }
  }

  /**
   * The generated output is garbage to read from the command line.
   */
  private fun <T : Set<Pair<Highlight, IntRange>>> Assert<T>.isEqualTo(expected: T) =
    transform { actual ->
      if (actual != expected) {
        println("\nGenerated highlights:")
        actual.forEach { (name, range) -> println("$name $range") }

        println("\nExpected highlights:")
        expected.forEach { (name, range) -> println("$name $range") }

        fail(expected, actual)
      }
    }
}
