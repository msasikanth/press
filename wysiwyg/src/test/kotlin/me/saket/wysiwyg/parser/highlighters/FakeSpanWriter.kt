package me.saket.wysiwyg.parser.highlighters

import me.saket.wysiwyg.parser.SpanWriter
import me.saket.wysiwyg.parser.node.HeadingLevel
import me.saket.wysiwyg.style.WysiwygStyle
import me.saket.wysiwyg.widgets.EditableText

internal class FakeSpanWriter(style: WysiwygStyle) : SpanWriter(style) {
  val highlights = mutableSetOf<Pair<Highlight, IntRange>>()

  private fun enqueue(highlight: Highlight, from: Int, to: Int) {
    highlights.add(highlight to from..to)
  }

  override fun addForegroundColor(color: Int, from: Int, to: Int) =
    enqueue(Color(color), from, to)

  override fun addItalics(from: Int, to: Int) =
    enqueue(Italics, from, to)

  override fun addBold(from: Int, to: Int) =
    enqueue(Bold, from, to)

  override fun addStrikethrough(from: Int, to: Int) =
    enqueue(Strikethrough, from, to)

  override fun addInlineCode(from: Int, to: Int) =
    enqueue(InlineCode, from, to)

  override fun addMonospaceTypeface(from: Int, to: Int) =
    enqueue(MonospacedTypeface, from, to)

  override fun addIndentedCodeBlock(from: Int, to: Int) =
    enqueue(IndentedCodeBlock, from, to)

  override fun addQuote(from: Int, to: Int) =
    enqueue(Quote, from, to)

  override fun addLeadingMargin(margin: Int, from: Int, to: Int) =
    enqueue(LeadingMargin(margin), from, to)

  override fun addHeading(level: HeadingLevel, from: Int, to: Int) =
    enqueue(Heading(level), from, to)

  override fun addClickableUrl(url: String, from: Int, to: Int) =
    enqueue(ClickableUrl(url), from, to)

  override fun addThematicBreak(syntax: String, from: Int, to: Int) =
    enqueue(ThematicBreak(syntax), from, to)

  override fun writeTo(text: EditableText) {
    TODO()
  }

  override fun clear() {
    TODO()
  }
}
