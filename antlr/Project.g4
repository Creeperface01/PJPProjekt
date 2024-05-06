grammar Project;

WS  :   [ \t\r\n]+ -> skip ;

COMMENT_SINGLELINE : '//' ~[\r\n]* -> skip ;
COMMENT_MULTILINE : '/*' .*? '*/' -> skip ;

program: r_statement* EOF ;

r_statement : r_read
          | r_write
          | r_block
          | r_if
          | r_while
          | r_variable_declaration
          | r_expression
          | r_assignment
          | SEMICOLON
          ;

r_read : READ var_name=VARIABLE_NAME (COMMA var_name=VARIABLE_NAME)* ;

r_write : WRITE r_expression (COMMA r_expression)* ;

r_variable_declaration : type=TYPE var_name=VARIABLE_NAME (COMMA var_name=VARIABLE_NAME)* ;

r_assignment : var_name=VARIABLE_NAME OPERATOR_ASSIGN (r_expression | r_assignment) ;

r_expression : r_numeric_expression #numericExpression
           | r_bool_expression #boolExpression
           | r_string_expression #stringExpression
           | var_name=VARIABLE_NAME #variableExpression
           ;

r_numeric_expression : UNARY_NUMERIC_OPERATOR unary=r_numeric_expression #numericUnaryExpression
                   | left=r_numeric_expression op=BINARY_NUMERIC_PRIORITY_OPERATOR right=r_numeric_expression #numericBinaryPriorityExpression
                   | left=r_numeric_expression op=BINARY_NUMERIC_OPERATOR right=r_numeric_expression #numericBinaryExpression
                   | PAREN_OPEN r_numeric_expression PAREN_CLOSE #numericParenthesisExpression
                   | value=NUMBER #numericValueExpression
                   ;

r_bool_expression : UNARY_BOOL_OPERATOR unary=r_bool_expression #boolUnaryExpression
                | left=r_bool_expression op=BINARY_BOOL_OPERATOR right=r_bool_expression #boolBinaryExpression
                | PAREN_OPEN r_bool_expression PAREN_CLOSE #boolParenthesisExpression
                | left=r_numeric_expression op=(COMPARISON_OPERATOR | EQUALITY_OPERATOR) right=r_numeric_expression #numericComparisonExpression
                | left=r_string_expression EQUALITY_OPERATOR right=r_string_expression #stringComparisonExpression
                | value=BOOL #boolValueExpression
                ;

r_string_expression : left=r_string_expression op=OPERATOR_CONCAT right=r_string_expression #stringConcatExpression
                  | value=STRING #stringValueExpression
                  ;

r_block: BLOCK_OPEN r_statement* BLOCK_CLOSE ;

r_if: IF PAREN_OPEN r_bool_expression PAREN_CLOSE (r_block | (r_statement SEMICOLON)) r_else? ;

r_else: ELSE (r_block | (r_statement SEMICOLON)) ;

r_while: WHILE PAREN_OPEN r_bool_expression PAREN_CLOSE r_block ;

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

BINARY_NUMERIC_PRIORITY_OPERATOR : OPERATOR_MUL | OPERATOR_DIV ;

BINARY_NUMERIC_OPERATOR : OPERATOR_ADD
                        | UNARY_NUMERIC_OPERATOR
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