// This is a generated file. Not intended for manual editing.
package language;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import language.impl.*;

public interface TranslateTypes {

  IElementType KEY = new TranslateElementType("KEY");

  IElementType SEPARATOR = new TranslateTokenType("SEPARATOR");
  IElementType SUBKEY = new TranslateTokenType("SUBKEY");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == KEY) {
        return new TranslateKeyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
