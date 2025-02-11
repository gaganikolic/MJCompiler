package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT
%xstate CHAR

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program" 	{return new_symbol(sym.PROGRAM, yytext());}
"break" 	{return new_symbol(sym.BREAK, yytext());}
"class" 	{return new_symbol(sym.CLASS, yytext());}
"else"		{return new_symbol(sym.ELSE, yytext());}
"const"		{return new_symbol(sym.CONST, yytext());}
"if"		{return new_symbol(sym.IF, yytext());}
"new" 		{return new_symbol(sym.NEW, yytext());}
"print"		{return new_symbol(sym.PRINT, yytext());}
"read" 		{return new_symbol(sym.READ, yytext());}
"return"	{return new_symbol(sym.RETURN, yytext());}
"void"		{return new_symbol(sym.VOID, yytext());}
"extends"	{return new_symbol(sym.EXTENDS, yytext());}
"continue" 	{return new_symbol(sym.CONTINUE, yytext());}
"for"		{return new_symbol(sym.FOR, yytext());}
"static" 	{return new_symbol(sym.STATIC, yytext());}
"namespace"	{return new_symbol(sym.NAMESPACE, yytext());}
"range"		{return new_symbol(sym.RANGE, yytext());}
"in"		{return new_symbol(sym.IN, yytext());}

"+"			{return new_symbol(sym.PLUS, yytext());}
"-"			{return new_symbol(sym.MINUS, yytext());}
"*" 		{return new_symbol(sym.STAR, yytext());}
"/" 		{return new_symbol(sym.SLASH, yytext());}
"%"			{return new_symbol(sym.PERCENT, yytext());}
"=="		{return new_symbol(sym.EQUALTO, yytext());}
"!=" 		{return new_symbol(sym.NOTEQUALTO, yytext());}
">"			{return new_symbol(sym.GREATER, yytext());}
">="		{return new_symbol(sym.GREATEREQUAL, yytext());}
"<"			{return new_symbol(sym.LESS, yytext());}
"<="		{return new_symbol(sym.LESSEQUAL, yytext());}
"&&"		{return new_symbol(sym.AND, yytext());}
"||" 		{return new_symbol(sym.OR, yytext());}
"=" 		{return new_symbol(sym.EQUAL, yytext());}
"++" 		{return new_symbol(sym.INCREMENT, yytext());}
"--"		{return new_symbol(sym.DECREMENT, yytext());}
";" 		{return new_symbol(sym.SEMI, yytext());}
":" 		{return new_symbol(sym.COLON, yytext());}
","			{return new_symbol(sym.COMMA, yytext());}
"."			{return new_symbol(sym.DOT, yytext());}
"("			{return new_symbol(sym.LPAREN, yytext());}
")"			{return new_symbol(sym.RPAREN, yytext());}
"["			{return new_symbol(sym.LSQUARE, yytext());}
"]" 		{return new_symbol(sym.RSQUARE, yytext());}
"{"			{return new_symbol(sym.LBRACE, yytext());}
"}" 		{return new_symbol(sym.RBRACE, yytext());}
"=>"		{return new_symbol(sym.ARROW, yytext());}

"'" {yybegin(CHAR);}
<CHAR> [a-zA-Z] {return new_symbol(sym.CHARCONST, yytext());}
<CHAR> "'" {yybegin(YYINITIAL);}

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" {yybegin(YYINITIAL);}

"true"  			{return new_symbol(sym.BOOLCONST, Boolean.TRUE);}
"false" 			{return new_symbol(sym.BOOLCONST, Boolean.FALSE);}
[0-9][0-9]* 			{return new_symbol(sym.NUMCONST, Integer.parseInt(yytext()));}
"'"."'" 			{return new_symbol(sym.CHARCONST, Character.valueOf(yytext().charAt(1)));}
[a-zA-Z][a-zA-Z0-9_]* 		{return new_symbol(sym.IDENT, yytext());}


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }










