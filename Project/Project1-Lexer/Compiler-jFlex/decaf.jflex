/* usercode */

package Compiler;

import java.io.FileReader;
import java.io.InputStreamReader;

%%
/* options */

%public
%class Scanner
%type String
%function NextToken
%line

%{
    public static void main(String[] args) {
        try {
            InputStreamReader reader = new InputStreamReader(System.in, "UTF-8");
            Scanner scanner = new Scanner(reader);
            do {
                String token = scanner.NextToken();
                if(token != null)
                    System.out.println(token);
            } while (!scanner.zzAtEOF);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public String CV;

    public Scanner(String fileName) throws java.io.IOException {
        this(new FileReader(fileName));
    }

    private String getFile(String include) {
        return include.substring(include.indexOf('\"') + 1, include.lastIndexOf('\"'));
    }

    private String symbol(DecafType type) {
        return type.toString();
    }

    private String symbol(DecafType type, Object value) {
        CV = value.toString();
        return type.toString();
    }
%}

InputCharacter = [^\r\n]

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t]

EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

Identifier = [:jletter:][:jletterdigit:]*

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexDigit          = [0-9a-fA-F]
DecimalIntegerLiteral = 0 | [1-9][0-9]*
IntegerLiteral = {DecimalIntegerLiteral} | {HexIntegerLiteral}

FloatLiteral1    = {DecimalIntegerLiteral} \. [0-9]*
FloatLiteral2    = \. [0-9]+
FloatLiteral3    = {DecimalIntegerLiteral}

FloatLiteral  = {FloatLiteral1} | {FloatLiteral2} | {FloatLiteral3}

SingleCharacter = [^\r\n\'\\] /* 32-126 ascii */

FileName = [^]+
Include = "#include" {WhiteSpace}* "\"" {FileName} "\""

%state CHARACTER_LITERAL, MULTILINE_COMMENT

%%
/* lexical rules */

<YYINITIAL> {
    "boolean"               { return symbol(DecafType.BOOLEAN); }
    "break"                 { return symbol(DecafType.BREAK); }
    "char"                  { return symbol(DecafType.CHAR); }
    "continue"              { return symbol(DecafType.CONTINUE); }
    "else"                  { return symbol(DecafType.ELSE); }
    "float"                 { return symbol(DecafType.FLOAT); }
    "for"                   { return symbol(DecafType.FOR); }
    "if"                    { return symbol(DecafType.IF); }
    "int"                   { return symbol(DecafType.INT); }
    "readchar"              { return symbol(DecafType.READCHAR); }
    "readfloat"             { return symbol(DecafType.READFLOAT); }
    "readint"               { return symbol(DecafType.READINT); }
    "return"                { return symbol(DecafType.RETURN); }
    "void"                  { return symbol(DecafType.VOID); }
    "while"                 { return symbol(DecafType.WHILE); }
    "writechar"             { return symbol(DecafType.WRITECHAR); }
    "writefloat"            { return symbol(DecafType.WRITEFLOAT); }
    "writeint"              { return symbol(DecafType.WRITEINT); }

    "true"                  { return symbol(DecafType.BOOLEAN_LITERAL, true); }
    "false"                 { return symbol(DecafType.BOOLEAN_LITERAL, false); }

    \'                      { yybegin(CHARACTER_LITERAL); }

    {IntegerLiteral}        { return symbol(DecafType.INT_LITERAL, yytext()); }

    {FloatLiteral}          { return symbol(DecafType.FLOAT_LITERAL, yytext()); }

    "("                     { return symbol(DecafType.LPAREN); }
    ")"                     { return symbol(DecafType.RPAREN); }
    "{"                     { return symbol(DecafType.LBRACE); }
    "}"                     { return symbol(DecafType.RBRACE); }
    "["                     { return symbol(DecafType.LBRACK); }
    "]"                     { return symbol(DecafType.RBRACK); }
    ";"                     { return symbol(DecafType.SEMICOLON); }
    ","                     { return symbol(DecafType.COMMA); }

    "="                     { return symbol(DecafType.EQ); }
    ">"                     { return symbol(DecafType.GT); }
    "<"                     { return symbol(DecafType.LT); }
    "!"                     { return symbol(DecafType.NOT); }
    "=="                    { return symbol(DecafType.EQEQ); }
    "<="                    { return symbol(DecafType.LTEQ); }
    ">="                    { return symbol(DecafType.GTEQ); }
    "!="                    { return symbol(DecafType.NOTEQ); }
    "&&"                    { return symbol(DecafType.ANDAND); }
    "||"                    { return symbol(DecafType.OROR); }
    "+"                     { return symbol(DecafType.PLUS); }
    "-"                     { return symbol(DecafType.MINUS); }
    "*"                     { return symbol(DecafType.MULT); }
    "/"                     { return symbol(DecafType.DIV); }
    "%"                     { return symbol(DecafType.MOD); }

    {Identifier}            { return symbol(DecafType.IDENTIFIER, yytext()); }

    {EndOfLineComment}      { /* ignore */ }
    "/*"                    { yybegin(MULTILINE_COMMENT); }

    {Include}               { yypushStream(new FileReader(getFile(yytext()))); }

    {WhiteSpace}            { /* ignore */ }
}

<CHARACTER_LITERAL> {
    {SingleCharacter}\'     { yybegin(YYINITIAL); return symbol(DecafType.CHAR_LITERAL, yytext().charAt(0)); }

    "\\t"\'                 { yybegin(YYINITIAL); return symbol(DecafType.CHAR_LITERAL, '\t'); }
    "\\n"\'                 { yybegin(YYINITIAL); return symbol(DecafType.CHAR_LITERAL, '\n'); }
    "\\'"\'                 { yybegin(YYINITIAL); return symbol(DecafType.CHAR_LITERAL, '\''); }
    "\\\\"\'                { yybegin(YYINITIAL); return symbol(DecafType.CHAR_LITERAL, '\\'); }
}

<MULTILINE_COMMENT> {
    "*/"                    { yybegin(YYINITIAL); }
    [^]                     { /* ignore */ }
}

[^]                         { return "Invalid Token at line " + yyline + " = " + yytext(); }

<<EOF>>                     { if (yymoreStreams()) yypopStream(); else return zzeof;}
