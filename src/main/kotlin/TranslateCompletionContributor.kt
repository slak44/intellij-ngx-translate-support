import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.ProcessingContext

class TranslateCompletionContributor : CompletionContributor() {
  init {
    val provider = object : CompletionProvider<CompletionParameters>() {
      override fun addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          result: CompletionResultSet
      ) {
        val text = (parameters.position.parent as JSLiteralExpression).stringValue!!
        // This cursor is usually +1 due to the string quote, but we drop chars below, so it's ok
        val cursor = parameters.offset - parameters.position.startOffset
        val toCurrent = text.substring(0, cursor).dropLastWhile { it != '.' }

        if (toCurrent.isEmpty() && text.isNotBlank()) {
          val item = LookupElementBuilder.create(text.dropLast(CompletionInitializationContext.DUMMY_IDENTIFIER.length) + ".")
              .bold()
              .withAutoCompletionPolicy(AutoCompletionPolicy.ALWAYS_AUTOCOMPLETE)
          result.addElement(PrioritizedLookupElement.withPriority(item, 200.0))
          return
        }

        val path = toCurrent.trimEnd('.').split('.')
        val props = translationReferenceVariantsFor(parameters.position.project, path)
        for (prop in props) {
          result.addElement(
              LookupElementBuilder.create("$toCurrent${prop.name}")
              .withPsiElement(prop)
              .withTypeText(prop.containingFile.name))
        }
      }
    }
    extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(JSLiteralExpression::class.java), provider)
  }
}
