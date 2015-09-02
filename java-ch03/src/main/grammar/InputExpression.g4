grammar InputExpression;

booleanExpression
  : expression
  | not
  | expression and 
  | expression or 
  | expression lt
  | bracketBooleanExpression
  ;
  
bracketBooleanExpression
  : '(' booleanExpression ')'
  ;

lt
  : '<' expression
  ;

not
  : '!' booleanExpression
  ;

and
  : '&&' booleanExpression
  ;

or
  : '||' booleanExpression
  ;
  
expression
  : term
  | '(' expression ')'
  | term plus
  ;

term 
  : name 
  | name dereference+
  | list
  | number
  | string
  ;

name
  : '$' ID
  ;
  
dereference
  : '.' ID
  ;

plus
  : '+' expression
  ;

list
  : '[' ']'
  | '[' expression ( ',' expression )* ']'
  ;
  
number
  : NUMBER
  ;

string
  : STRING
  ;

ID
  : [a-zA-Z_][a-zA-Z_0-9]*
  ;

NUMBER
  : [0-9]+ ('.'[0-9]*)?
  ;

STRING
  : '"' ( ESC |  ~('"'|'\\'|'\n'|'\r') )* '"'     
  ;

fragment ESC
  :   '\\'
      (   'n'
      |   'r'
      |   't'
      |   'b'
      |   'f'
      |   '"'
      |   '\''
      |   '/'
      |   '\\'
      |   ('u')+ 
      )
  ;

HEX_DIGIT
  : [a-fA-F0-9]+
  ;
  
WHITESPACE 
  : [ \t\r\n]+ -> skip
  ; 
    