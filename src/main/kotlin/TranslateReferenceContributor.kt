import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import language.PathKeyLiteral
import language.TranslateKey

class TranslateReferenceContributor : PsiReferenceContributor() {
  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(PlatformPatterns.psiElement(PathKeyLiteral::class.java), object : PsiReferenceProvider() {
      override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return arrayOf(PropertyJsonReference(element as PathKeyLiteral, TextRange(0, element.textLength)))
      }
    })
  }
}
