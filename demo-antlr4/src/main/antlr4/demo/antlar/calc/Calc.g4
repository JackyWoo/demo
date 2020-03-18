grammar Calc;
calc: expr NEWLINE*;

expr:	expr op=('*'|'/') expr # mulDiv
    |	expr op=('+'|'-') expr # addSub
    |	DIGITAL             # digital
    |	'(' expr ')'        # childExpr
    ;

MUL : '*';                  // type of op
DIV : '/';
ADD : '+';
SUB : '-';

NEWLINE: ('\t'?'\n')+;
DIGITAL: [0-9]+;
WS: [ \t]+ -> skip;