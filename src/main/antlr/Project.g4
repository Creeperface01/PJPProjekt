grammar Project;

WS  :   [ \t\r\n]+ -> skip ;

COMMENT_SINGLELINE : '//' ~[\r\n]* -> skip ;
COMMENT_MULTILINE : '/*' .*? '*/' -> skip ;

program: statement* EOF ;

statement : read
          | write
          | block
          | if
          | while
          | variable_declaration
          | expression
          | assignment
          | SEMICOLON
          ;

read : READ VARIABLE_NAME (COMMA VARIABLE_NAME)* ;

write : WRITE expression (COMMA expression)* ;

variable_declaration : TYPE VARIABLE_NAME (COMMA VARIABLE_NAME)* ;

assignment : (VARIABLE_NAME OPERATOR_ASSIGN)+ expression ;

expression : numeric_expression
           | bool_expression
           | string_expression
           ;

numeric_expression : UNARY_NUMERIC_OPERATOR numeric_expression
                   | numeric_expression BINARY_NUMERIC_OPERATOR numeric_expression
                   | PAREN_OPEN numeric_expression PAREN_CLOSE
                   | VARIABLE_NAME
                   | NUMBER
                   ;

bool_expression : UNARY_BOOL_OPERATOR bool_expression
                | bool_expression BINARY_BOOL_OPERATOR bool_expression
                | PAREN_OPEN bool_expression PAREN_CLOSE
                | BOOL
                | VARIABLE_NAME
                | numeric_expression (COMPARISON_OPERATOR | EQUALITY_OPERATOR) numeric_expression
                ;

string_expression : string_expression (OPERATOR_CONCAT | EQUALITY_OPERATOR) string_expression
                  | VARIABLE_NAME
                  | STRING
                  ;

block: BLOCK_OPEN statement* BLOCK_CLOSE ;

if: IF PAREN_OPEN bool_expression PAREN_CLOSE (block | (statement SEMICOLON)) else? ;

else: ELSE (block | (statement SEMICOLON)) ;

while: WHILE PAREN_OPEN bool_expression PAREN_CLOSE block ;

TYPE : LITERAL_INT
     | LITERAL_FLOAT
     | LITERAL_BOOLEAN
     | LITERAL_STRING
     ;

COMPARISON_OPERATOR : OPERATOR_GT
                    | OPERATOR_LT
                    | OPERATOR_GTE
                    | OPERATOR_LTE
                    ;

EQUALITY_OPERATOR : OPERATOR_EQ | OPERATOR_NEQ ;

BINARY_BOOL_OPERATOR : OPERATOR_AND
                     | OPERATOR_OR
                     ;

UNARY_BOOL_OPERATOR : OPERATOR_NOT ;

UNARY_NUMERIC_OPERATOR : OPERATOR_UNARY_MINUS ;

BINARY_NUMERIC_OPERATOR : OPERATOR_ADD
                        | UNARY_NUMERIC_OPERATOR
                        | OPERATOR_MUL
                        | OPERATOR_DIV
                        | OPERATOR_MOD
                        ;

NUMBER : INT | FLOAT ;

READ : 'read' ;
WRITE : 'write' ;
IF : 'if' ;
ELSE : 'else' ;
WHILE : 'while' ;

PAREN_OPEN : '(' ;
PAREN_CLOSE : ')' ;

BLOCK_OPEN : '{' ;
BLOCK_CLOSE : '}' ;

LITERAL_INT : 'int' ;
LITERAL_FLOAT : 'float' ;
LITERAL_BOOLEAN : 'bool' ;
LITERAL_STRING : 'string' ;
SEMICOLON : ';' ;
COMMA : ',' ;

OPERATOR_UNARY_MINUS : '-' ;
OPERATOR_ADD : '+' ;
OPERATOR_MUL : '*' ;
OPERATOR_DIV : '/' ;
OPERATOR_MOD : '%' ;
OPERATOR_AND : '&&' ;
OPERATOR_OR : '||' ;
OPERATOR_NOT : '!' ;
OPERATOR_EQ : '==' ;
OPERATOR_NEQ : '!=' ;
OPERATOR_GT : '>' ;
OPERATOR_LT : '<' ;
OPERATOR_GTE : '>=' ;
OPERATOR_LTE : '<=' ;
OPERATOR_CONCAT : '.' ;
OPERATOR_ASSIGN : '=' ;

BOOL : TRUE | FALSE ;
INT :   [0-9]+ ;
FLOAT: INT ('.' [0-9]+)? ;
TRUE : 'true' ;
FALSE : 'false' ;

VARIABLE_NAME : [a-zA-Z_][a-zA-Z0-9_]* ;

STRING : '"' (ESC | ~(["\\\r\n]))* '"';
ESC : '\\"';