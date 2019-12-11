package me.saket.wysiwyg.parser.highlighters

import me.saket.wysiwyg.parser.node.HeadingLevel
import me.saket.wysiwyg.style.WysiwygStyle

/**
 * Generates test data for comparing generated markdown spans in [RootNodeHighlighterTest].
 */
internal class FakeSpannedBuilder(private val style: WysiwygStyle) {
  private var textLength = 0
  private val highlights = mutableSetOf<Pair<Highlight, IntRange>>()

  private fun withHighlight(name: Highlight, builderAction: FakeSpannedBuilder.() -> Unit): FakeSpannedBuilder {
    val from = textLength
    builderAction()
    val to = textLength
    highlights += name to from..to
    return this
  }

  fun syntax(text: String): FakeSpannedBuilder {
    return color(style.syntaxColor) {
      text(text)
    }
  }

  fun text(text: String): FakeSpannedBuilder {
    textLength += text.length
    return this
  }

  fun color(color: Int, builderAction: FakeSpannedBuilder.() -> Unit) =
    withHighlight(Color(color), builderAction)

  fun bold(text: String) =
    withHighlight(Bold) {
      syntax("**") + text(text) + syntax("**")
    }

  fun italics(text: String) =
    withHighlight(Italics) {
      syntax("*") + text(text) + syntax("*")
    }

  fun strikethrough(text: String) =
    withHighlight(Strikethrough) {
      syntax("~~") + color(style.strikethroughTextColor) { text(text) } + syntax("~~")
    }

  fun build() = highlights

  /** For nicer looking usages. */
  operator fun plus(o: FakeSpannedBuilder) = this
  operator fun plus(text: String) = text(text)
}

internal interface Highlight
internal object Bold : Highlight { override fun toString() = "Bold" }
internal object Italics : Highlight { override fun toString() = "Italics" }
internal object Strikethrough : Highlight { override fun toString() = "Strikethrough" }
internal object InlineCode : Highlight { override fun toString() = "InlineCode" }
internal object MonospacedTypeface : Highlight { override fun toString() = "MonospacedTypeface" }
internal object IndentedCodeBlock : Highlight { override fun toString() = "IndentedCodeBlock" }
internal object Quote : Highlight { override fun toString() = "Quote" }
internal data class Color(val color: String) : Highlight { constructor(color: Int) : this(color.toHexColor()) }
internal data class LeadingMargin(val margin: Int) : Highlight
internal data class Heading(val level: HeadingLevel) : Highlight
internal data class ClickableUrl(val url: String) : Highlight
internal data class ThematicBreak(val syntax: String) : Highlight

private fun Int.toHexColor(): String =
  String.format("#%06X", 0xFFFFFF and this)
