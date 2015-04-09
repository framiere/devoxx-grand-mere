grammar GrandMere;

parse
 : expr
 ;

expr
 : '(' expr ')'                                # subExpr
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
 ;

INT
 : [0-9]+
 ;

FLOAT
 : INT+ '.' INT*  // 1. or 1.1
 |      '.' INT+  // .1
 ;

EXP
 : INT '.' INT 'E' [-] INT  // 1.1E-4
 ;

OTHER
 : .
 ;