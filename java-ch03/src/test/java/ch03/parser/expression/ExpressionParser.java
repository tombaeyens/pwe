package ch03.parser.expression;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import ch03.data.InputExpression;
import ch03.parser.expression.input.InputExpressionLexer;
import ch03.parser.expression.input.InputExpressionParser;


/**
 * @author Tom Baeyens
 */
public class ExpressionParser {

  public InputExpression parseInputExpression(String expressionText) {
    InputExpressionBuilder inputExpressionBuilder = new InputExpressionBuilder();
    InputExpressionLexer l = new InputExpressionLexer(new ANTLRInputStream(expressionText));
    InputExpressionParser p = new InputExpressionParser(new CommonTokenStream(l));
    p.addParseListener(inputExpressionBuilder);
    p.booleanExpression();
    return inputExpressionBuilder.getExpression();
  }
}
