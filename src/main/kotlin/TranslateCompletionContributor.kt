import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonObject
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.ProcessingContext

private fun getCurrentItem(parameters: CompletionParameters): String {
  val text = (parameters.position.parent as JSLiteralExpression).stringValue!!
  // This cursor is usually +1 due to the string quote, but we drop chars below, so it's ok
  val cursor = parameters.offset - parameters.position.startOffset
  return text.substring(0, cursor).dropLastWhile { it != '.' }
}

private val basicProvider = object : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet
  ) {
    val toCurrent = getCurrentItem(parameters)
    val path = if (toCurrent.isEmpty()) emptyList() else toCurrent.trimEnd('.').split('.')

    val props = translationReferenceVariantsFor(parameters.position.project, path)
    for (prop in props) {
      val extraDot = if (prop.value is JsonObject) "." else ""
      result.addElement(
          LookupElementBuilder.create("$toCurrent${prop.name}$extraDot")
              .withPsiElement(prop)
              .withTypeText(prop.containingFile.name))
    }
  }
}

class TranslateCompletionContributor : CompletionContributor() {
  init {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(JSLiteralExpression::class.java), basicProvider)
  }
}
