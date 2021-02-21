// This is a generated file. Not intended for manual editing.
package language;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class TranslateVisitor extends PsiElementVisitor {

  public void visitKey(@NotNull TranslateKey o) {
    visitPathKeyLiteral(o);
  }

  public void visitPathKeyLiteral(@NotNull PathKeyLiteral o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
