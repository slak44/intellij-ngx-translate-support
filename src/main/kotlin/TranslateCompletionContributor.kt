import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

private fun getCurrentItem(parameters: CompletionParameters): String {
  val text = (parameters.position.parent as JSLiteralExpression).stringValue!!
  // This cursor is usually +1 due to the string quote, but we drop chars below, so it's ok
  val cursor = parameters.offset - parameters.position.startOffsetInParent
  return text.substring(0, cursor).dropLastWhile { it != '.' }
}

private val basicProvider = object : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet
  ) {
    val toCurrent = getCurrentItem(parameters)

    for (value in I18NFileIndex.valuesFor(toCurrent.trimEnd('.'), parameters.position.project)) {
      val lookupElement = LookupElementBuilder.create(value)
      result.addElement(lookupElement)
    }
  }
}

class TranslateCompletionContributor : CompletionContributor() {
  init {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(JSLiteralExpression::class.java), basicProvider)
  }
}
