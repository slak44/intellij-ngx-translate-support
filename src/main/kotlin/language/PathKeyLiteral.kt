package language

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

interface PathKeyLiteral : PsiLiteralValue, PsiNamedElement {
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
      val keyNode = node.findChildByType(TranslateTypes.SUBKEY)
      return keyNode?.text?.replace("\\\\ ", " ")
    }

    @JvmStatic
    fun TranslateKey.getName(): String? {
      return value
    }

    @JvmStatic
    fun TranslateKey.setName(newName: String): PsiElement {
      val keyNode = node.findChildByType(TranslateTypes.SUBKEY)
      if (keyNode != null) {
        val property = createProperty(project, newName)
        val newKeyNode = property.firstChild.node
        node.replaceChild(keyNode, newKeyNode)
      }
      return this
    }
  }
}

fun createProperty(project: Project, name: String): TranslateKey {
  val file = createFile(project, name)
  return file.firstChild as TranslateKey
}

fun createFile(project: Project, text: String): TranslateFile {
  val name = "dummy.ngxtranslateaccess"
  return PsiFileFactory.getInstance(project).createFileFromText(name, TranslateFileType, text) as TranslateFile
}

