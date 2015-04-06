grammar GrandMere;

parse
 : expr
 ;

expr
 : <assoc=right> expr '^' expr                 # exponentExpr
 | '(' expr ')'                                # subExpr
 | '-' expr                                    # unaryMinusExpr
 | '+' expr                                    # unaryPlusExpr
 | expr high_priority_operation expr           # highPriorityOperationExpr
 | expr low_priority_operation expr            # lowPriorityOperationExpr
 | atom                                        # atomExpr
 ;

high_priority_operation
 : '*'
 | '/'
 ;

low_priority_operation
 : '+'
 | '-'
 ;

atom
 : INT         # intAtom
 | FLOAT       # floatAtom
 | EXP         # expAtom
 | BOOLEAN     # booleanAtom
 | STRING      # stringAtom
 ;

BOOLEAN
 : 'true'
 | 'false'
 ;

INT
 : [0-9]+
 ;

FLOAT
 : [0-9]+ '.' [0-9]*  // 1. or 1.1
 |        '.' [0-9]+  // .1
 ;

EXP
 : INT '.' INT 'E' [-] INT  // 1.1E-4
 ;

string
 : STRING
 ;

STRING
 : '"' ~('"')+ '"'
 ;

MINUS: '-';

PLUS: '+';

SPACE
 : [ \t\r\n] -> channel(HIDDEN)
 ;

OTHER
 : .
 ;