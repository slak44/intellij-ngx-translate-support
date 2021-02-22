import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import language.PathKeyLiteral

class TranslateRefactoringSupportProvider : RefactoringSupportProvider() {
  override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
    return element is PathKeyLiteral
  }
}
