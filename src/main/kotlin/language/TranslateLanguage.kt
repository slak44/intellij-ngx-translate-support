package language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import javax.swing.Icon


object TranslateLanguage : Language("ngx-translate property access")

open class TranslateTokenType(debugName: String) : IElementType(debugName, TranslateLanguage) {
  override fun toString(): String {
    return "TranslateTokenType." + super.toString()
  }
}

open class TranslateElementType(debugName: String) : IElementType(debugName, TranslateLanguage)

class TranslateLexerAdapter : FlexAdapter(TranslateLexer(null))

class TranslateIcons {
  companion object {
    @JvmStatic
    val FILE = IconLoader.getIcon("/icons/jar-gray.png")
  }
}

object TranslateFileType : LanguageFileType(TranslateLanguage) {
  override fun getName(): String {
    return "ngx-translate property access"
  }

  override fun getDescription(): String {
    return "Ngx-translate dot notation"
  }

  override fun getDefaultExtension(): String {
    return "ngxtranslateaccess"
  }

  override fun getIcon(): Icon {
    return TranslateIcons.FILE
  }
}

class TranslateFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, TranslateLanguage) {
  override fun getFileType(): FileType = TranslateFileType
}

class TranslateParserDefinition : ParserDefinition {
  object File : IFileElementType(TranslateLanguage)

  override fun createLexer(project: Project?): Lexer = TranslateLexerAdapter()

  override fun createParser(project: Project?): PsiParser = TranslateParser()

  override fun getFileNodeType(): IFileElementType = File

  override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

  override fun createElement(node: ASTNode?): PsiElement = TranslateTypes.Factory.createElement(node)

  override fun createFile(viewProvider: FileViewProvider): PsiFile = TranslateFile(viewProvider)
}

class TranslateSyntaxHighlighter : SyntaxHighlighterBase() {
  override fun getHighlightingLexer(): Lexer = TranslateLexerAdapter()

  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = when (tokenType) {
    TranslateTypes.SEPARATOR -> arrayOf(separator)
    TranslateTypes.KEY, TranslateTypes.SUBKEY -> arrayOf(key)
    TokenType.BAD_CHARACTER -> arrayOf(badCharacter)
    else -> emptyArray()
  }

  object Factory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
      return TranslateSyntaxHighlighter()
    }
  }

  companion object {
    private val separator = TextAttributesKey.createTextAttributesKey(
        "TRANSLATE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)

    private val key = TextAttributesKey.createTextAttributesKey(
        "TRANSLATE_KEY", DefaultLanguageHighlighterColors.STRING)

    private val badCharacter =
        TextAttributesKey.createTextAttributesKey("TRANSLATE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
  }
}

