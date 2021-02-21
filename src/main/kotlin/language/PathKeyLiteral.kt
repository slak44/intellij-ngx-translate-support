package language

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

interface PathKeyLiteral : PsiLiteralValue {
  override fun getValue(): String?
}

abstract class PathKeyLiteralImpl(node: ASTNode) : ASTWrapperPsiElement(node), PathKeyLiteral {
  override fun getReferences(): Array<PsiReference> {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this)
  }
}

class TranslatePsiImplUtil {
  @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
  companion object {
    @JvmStatic
    fun TranslateKey.getValue(): String? {
      val keyNode = this.node.findChildByType(TranslateTypes.SUBKEY)
      return keyNode?.text?.replace("\\\\ ", " ")
    }
  }
}
