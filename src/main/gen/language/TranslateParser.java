// This is a generated file. Not intended for manual editing.
package language;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static language.TranslateTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TranslateParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return json_selector(b, l + 1);
  }

  /* ********************************************************** */
  // key (SEPARATOR key)*
  static boolean json_selector(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_selector")) return false;
    if (!nextTokenIs(b, SUBKEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = key(b, l + 1);
    r = r && json_selector_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SEPARATOR key)*
  private static boolean json_selector_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_selector_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!json_selector_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "json_selector_1", c)) break;
    }
    return true;
  }

  // SEPARATOR key
  private static boolean json_selector_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_selector_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEPARATOR);
    r = r && key(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SUBKEY
  public static boolean key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key")) return false;
    if (!nextTokenIs(b, SUBKEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SUBKEY);
    exit_section_(b, m, KEY, r);
    return r;
  }

}
