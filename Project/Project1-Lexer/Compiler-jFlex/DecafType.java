package Compiler;

public enum DecafType {
    ANDAND("&&"),
    BOOLEAN(),
    BOOLEAN_LITERAL(),
    BREAK(),
    CHAR(),
    CHAR_LITERAL(),
    CONTINUE(),
    COMMA(","),
    DIV("/"),
    ELSE(),
    EQ("="),
    EQEQ("=="),
    FLOAT(),
    FLOAT_LITERAL(),
    FOR(),
    GT(">"),
    GTEQ(">="),
    IDENTIFIER("id"),
    IF(),
    INT(),
    INT_LITERAL(),
    LBRACE("{"),
    LBRACK("["),
    LPAREN("("),
    LT("<"),
    LTEQ("<="),
    MINUS("-"),
    MOD("%"),
    MULT("*"),
    NOT("!"),
    NOTEQ("!="),
    OROR("||"),
    PLUS("+"),
    RBRACE("}"),
    RBRACK("]"),
    READCHAR(),
    READFLOAT(),
    READINT(),
    RETURN(),
    RPAREN(")"),
    SEMICOLON(";"),
    VOID(),
    WHILE(),
    WRITECHAR(),
    WRITEFLOAT(),
    WRITEINT();

    private String tokenString;

    DecafType() {
        tokenString = name().toLowerCase();
    }

    DecafType(String name) {
        tokenString = name;
    }

    @Override
    public String toString() {
        return tokenString;
    }
}
