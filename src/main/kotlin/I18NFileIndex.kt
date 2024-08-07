import com.intellij.json.JsonFileType
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import java.io.DataInput
import java.io.DataOutput

class I18NFileIndex : FileBasedIndexExtension<String, List<String>>() {
  override fun getName(): ID<String, List<String>> = Util.NAME

  override fun getIndexer(): DataIndexer<String, List<String>, FileContent> = DataIndexer { fileContent ->
    val jsonObject = fileContent.psiFile.children.find { it is JsonObject } ?: return@DataIndexer emptyMap()
    val map = mutableMapOf<String, MutableList<String>>()
    val path = mutableListOf<String>()

    fun addToMap(pathKey: String, property: JsonProperty, isObject: Boolean) {
      val lastKey = if (isObject) "${property.name}." else property.name
      map.getOrPut(pathKey, ::mutableListOf) += if (pathKey.isEmpty()) lastKey else "$pathKey.$lastKey"
    }

    fun dfs(property: JsonProperty) {
      val pathKey = path.joinToString(".")
      path += property.name
      when (val item = property.value) {
        is JsonStringLiteral -> {
          addToMap(pathKey, property, false)
        }
        is JsonObject -> {
          addToMap(pathKey, property, true)
          for (subProp in item.children.filterIsInstance<JsonProperty>()) {
            dfs(subProp)
          }
        }
        else -> {
          // Not string, not nested object
        }
      }
      path.removeLast()
    }

    for (rootProperty in jsonObject.children.filterIsInstance<JsonProperty>()) {
      dfs(rootProperty)
    }

    return@DataIndexer map.toMap()
  }

  override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

  override fun getValueExternalizer(): DataExternalizer<List<String>> = object : DataExternalizer<List<String>> {
    override fun save(out: DataOutput, value: List<String>?) {
      out.writeUTF(value?.joinToString(",") ?: "")
    }

    override fun read(`in`: DataInput): List<String> {
      return `in`.readUTF().split(",")
    }
  }

  override fun getVersion(): Int = 0

  override fun getInputFilter(): FileBasedIndex.InputFilter = FileBasedIndex.InputFilter {
    it.fileType == JsonFileType.INSTANCE && "i18n" in it.path
  }

  override fun dependsOnFileContent(): Boolean = true

  object Util {
    val NAME = ID.create<String, List<String>>("slak.I18NFileIndex")

    fun valuesFor(key: String, project: Project): List<String> {
      return FileBasedIndex.getInstance().getValues(NAME, key, GlobalSearchScope.projectScope(project)).flatten()
    }
  }
}
