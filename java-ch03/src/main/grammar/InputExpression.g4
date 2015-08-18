grammar InputExpression;

expression
  : term
  | term '+' expression
  ;
  
term 
  : ID 
  | ID dereference+
  | list
  ;

dereference
  : '.' ID
  ;

list
  : '[' ']'
  | '[' expression ( ',' expression )* ']'
  ;

PLUS : '+';
ID   : [a-zA-Z0-9]+;
WS   : [ \t\r\n]+ -> skip; 
