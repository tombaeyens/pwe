package ch03.data.expression.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import ch03.data.expression.parser.input.InputExpressionListener;
import ch03.data.expression.parser.input.InputExpressionParser.DereferenceContext;
import ch03.data.expression.parser.input.InputExpressionParser.ExpressionContext;
import ch03.data.expression.parser.input.InputExpressionParser.ListContext;
import ch03.data.expression.parser.input.InputExpressionParser.TermContext;


/**
 * @author Tom Baeyens
 */
public class InputExpressionBuilder implements InputExpressionListener {
  
  
  
  void log(String msg) {
    System.out.println(msg);
  }
  
  @Override
  public void enterExpression(ExpressionContext expressionContext) {
    log("enter expression "+expressionContext.getText());
  }

  @Override
  public void exitExpression(ExpressionContext ctx) {
    log("exit expression "+ctx.getText()+" "+ctx.getStart());
  }

  @Override
  public void enterDereference(DereferenceContext ctx) {
    log("enter dereference "+ctx.getText()+" "+ctx.getStart());
  }

  @Override
  public void exitDereference(DereferenceContext ctx) {
    log("exit dereference "+ctx.getChild(1).getText());
  }
  

  @Override
  public void enterTerm(TermContext ctx) {
    log("enter term "+ctx.getText());
  }

  @Override
  public void exitTerm(TermContext ctx) {
    log("exit term "+ctx.getText());
  }

  @Override
  public void enterList(ListContext ctx) {
    log("enter list "+ctx.getText());
  }

  @Override
  public void exitList(ListContext ctx) {
    log("exit list "+ctx.getText());
  }


  @Override
  public void enterEveryRule(ParserRuleContext arg0) {
  }

  @Override
  public void exitEveryRule(ParserRuleContext arg0) {
  }

  @Override
  public void visitErrorNode(ErrorNode errorNode) {
  }

  @Override
  public void visitTerminal(TerminalNode terminalNode) {
  }
}
