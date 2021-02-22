// This is a generated file. Not intended for manual editing.
package language.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static language.TranslateTypes.*;
import language.PathKeyLiteralImpl;
import language.*;

public class TranslateKeyImpl extends PathKeyLiteralImpl implements TranslateKey {

  public TranslateKeyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TranslateVisitor visitor) {
    visitor.visitKey(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TranslateVisitor) accept((TranslateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public String getValue() {
    return TranslatePsiImplUtil.getValue(this);
  }

  @Override
  @Nullable
  public String getName() {
    return TranslatePsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public PsiElement setName(@NotNull String newName) {
    return TranslatePsiImplUtil.setName(this, newName);
  }

}
