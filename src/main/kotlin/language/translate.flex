package language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import language.TranslateTypes;

%%

%class TranslateLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

SEPARATOR="."
KEY=[A-Z0-9_-]

%state WAITING_VALUE

%%

<YYINITIAL> {KEY}+ { yybegin(WAITING_VALUE); return TranslateTypes.SUBKEY; }

<WAITING_VALUE> {SEPARATOR} { yybegin(YYINITIAL); return TranslateTypes.SEPARATOR; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
