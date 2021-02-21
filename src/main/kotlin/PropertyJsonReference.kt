import com.intellij.json.JsonFileType
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import language.PathKeyLiteral

fun findByKey(project: Project, key: List<String>): List<JsonProperty> {
  val virtualFiles = FileTypeIndex.getFiles(JsonFileType.INSTANCE, GlobalSearchScope.allScope(project))
  val propsFound = mutableListOf<JsonProperty>()
  fileLoop@ for (virtualFile in virtualFiles) {
    if ("i18n" !in virtualFile.path) continue
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile!!) ?: continue
    var lastChild: PsiElement = psiFile.children.find { it is JsonObject } ?: continue
    if (key.isEmpty()) {
      propsFound += lastChild.children.filterIsInstance<JsonProperty>()
      continue
    }
    for (subKey in key) {
      val foundProp = advanceElementStructure(lastChild, subKey) ?: continue@fileLoop
      val nextPsi = foundProp.value ?: continue@fileLoop
      lastChild = nextPsi
    }
    propsFound += lastChild.parent as JsonProperty
  }

  return propsFound
}

fun advanceElementStructure(parentRef: PsiElement, keyFragment: String): JsonProperty? {
  return parentRef.children.filterIsInstance<JsonProperty>().find { it.name == keyFragment }
}

fun currentElemPath(element: PathKeyLiteral, includeCurrent: Boolean): List<String> {
  val key = element.text

  return PsiTreeUtil.getChildrenOfType(element.parent, PathKeyLiteral::class.java)
      .takeWhile { it.value != key }
      .map { it.value!! }
      .let { if (includeCurrent) it + key else it }
}

fun translationReferenceVariantsFor(project: Project, path: List<String>): List<JsonProperty> {
  val props = findByKey(project, path)
  return props
      .mapNotNull { (it.value as? JsonObject)?.children?.toList() }
      .flatten()
      .filterIsInstance<JsonProperty>()
}

class PropertyJsonReference(
    element: PathKeyLiteral,
    textRange: TextRange,
) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {
  override fun resolve(): PsiElement? {
    val resolveResults = multiResolve(false)
    return if (resolveResults.size == 1) resolveResults[0].element else null
  }

  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    val path = currentElemPath(element as PathKeyLiteral, true)
    if (path.isEmpty()) return emptyArray()

    return findByKey(element.project, path).map { PsiElementResolveResult(it) }.toTypedArray()
  }

  override fun getVariants(): Array<Any> {
    val path = currentElemPath(element as PathKeyLiteral, false)
    if (path.isEmpty()) return emptyArray()
    return translationReferenceVariantsFor(element.project, path).toTypedArray()
  }
}
