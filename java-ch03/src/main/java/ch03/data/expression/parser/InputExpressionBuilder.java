package ch03.data.expression.parser;

import java.util.LinkedList;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import ch03.data.DereferenceExpression;
import ch03.data.InputExpression;
import ch03.data.LessThanExpression;
import ch03.data.NameExpression;
import ch03.data.PlusExpression;
import ch03.data.TypedValueExpression;
import ch03.data.expression.parser.input.InputExpressionListener;
import ch03.data.expression.parser.input.InputExpressionParser.AndContext;
import ch03.data.expression.parser.input.InputExpressionParser.BooleanExpressionContext;
import ch03.data.expression.parser.input.InputExpressionParser.BracketBooleanExpressionContext;
import ch03.data.expression.parser.input.InputExpressionParser.DereferenceContext;
import ch03.data.expression.parser.input.InputExpressionParser.ExpressionContext;
import ch03.data.expression.parser.input.InputExpressionParser.ListContext;
import ch03.data.expression.parser.input.InputExpressionParser.LtContext;
import ch03.data.expression.parser.input.InputExpressionParser.NameContext;
import ch03.data.expression.parser.input.InputExpressionParser.NotContext;
import ch03.data.expression.parser.input.InputExpressionParser.NumberContext;
import ch03.data.expression.parser.input.InputExpressionParser.OrContext;
import ch03.data.expression.parser.input.InputExpressionParser.PlusContext;
import ch03.data.expression.parser.input.InputExpressionParser.StringContext;
import ch03.data.expression.parser.input.InputExpressionParser.TermContext;
import ch03.data.types.NumberType;
import ch03.data.types.StringType;


/**
 * @author Tom Baeyens
 */
public class InputExpressionBuilder implements InputExpressionListener {
  
  LinkedList<InputExpression> expressions;
  
  public InputExpressionBuilder() {
    expressions = new LinkedList<InputExpression>();
  }

  public InputExpression getExpression() {
    return !expressions.isEmpty() ? expressions.get(0) : null;
  }
  
  void log(String msg) {
    System.out.println(msg);
  }
  
  
  
  @Override
  public void enterBooleanExpression(BooleanExpressionContext ctx) {
    log("enter boolean expression "+ctx.getText());
  }

  @Override
  public void exitBooleanExpression(BooleanExpressionContext ctx) {
    log("exit boolean expression "+ctx.getText());
  }

  @Override
  public void enterExpression(ExpressionContext ctx) {
    log("enter expression "+ctx.getText());
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
    String field = ctx.getChild(1).getText();
    InputExpression right = expressions.removeLast();
    expressions.add(new DereferenceExpression(right, field));
  }
  
  @Override
  public void enterName(NameContext ctx) {
  }

  @Override
  public void exitName(NameContext ctx) {
    String name = ctx.getChild(1).getText();
    log("exit name "+name);
    expressions.add(new NameExpression(name));
  }
  
  @Override
  public void enterPlus(PlusContext ctx) {
    log("enter plus "+ctx.getText());
  }

  @Override
  public void exitPlus(PlusContext ctx) {
    InputExpression right = expressions.removeLast();
    InputExpression left = expressions.removeLast();
    log("exit plus: "+left+" + "+right);
    expressions.add(new PlusExpression(left, right));
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
  public void enterLt(LtContext ctx) {
    log("enter lt"+ctx.getText());
  }

  @Override
  public void exitLt(LtContext ctx) {
    InputExpression right = expressions.removeLast();
    InputExpression left = expressions.removeLast();
    expressions.add(new LessThanExpression(left, right));
  }

  @Override
  public void enterNot(NotContext ctx) {
    log("enter not "+ctx.getText());
  }

  @Override
  public void exitNot(NotContext ctx) {
    log("exit not "+ctx.getText());
  }

  @Override
  public void enterAnd(AndContext ctx) {
    log("enter and "+ctx.getText());
  }

  @Override
  public void exitAnd(AndContext ctx) {
    log("exit and "+ctx.getText());
  }

  @Override
  public void enterOr(OrContext ctx) {
    log("enter or "+ctx.getText());
  }

  @Override
  public void exitOr(OrContext ctx) {
    log("exit or "+ctx.getText());
  }

  @Override
  public void enterNumber(NumberContext ctx) {
    log("enter number "+ctx.getText());
  }

  @Override
  public void exitNumber(NumberContext ctx) {
    Number number = null;
    String numberString = ctx.getText();
    if (numberString!=null) {
      if (numberString.contains(".")) {
        number = Double.parseDouble(numberString);
      } else {
        number = Long.parseLong(numberString);
      }
    }
    log("exit number "+number);
    expressions.add(new TypedValueExpression(NumberType.INSTANCE, number));
  }

  @Override
  public void enterString(StringContext ctx) {
  }

  @Override
  public void exitString(StringContext ctx) {
    String quotedString = ctx.getText();
    String string = quotedString.substring(1, quotedString.length()-1);
    log("exit string "+quotedString);
    expressions.add(new TypedValueExpression(StringType.INSTANCE, string));
  }

  @Override
  public void enterBracketBooleanExpression(BracketBooleanExpressionContext ctx) {
    log("enter bracket expression "+ctx.getText());
  }

  @Override
  public void exitBracketBooleanExpression(BracketBooleanExpressionContext ctx) {
    log("exit bracket expression "+ctx.getText());
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
