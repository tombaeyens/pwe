// Generated from InputExpression.g4 by ANTLR 4.5.1
package ch03.data.expression.parser.input;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link InputExpressionParser}.
 */
public interface InputExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(InputExpressionParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(InputExpressionParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#bracketBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracketBooleanExpression(InputExpressionParser.BracketBooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#bracketBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracketBooleanExpression(InputExpressionParser.BracketBooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#lt}.
	 * @param ctx the parse tree
	 */
	void enterLt(InputExpressionParser.LtContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#lt}.
	 * @param ctx the parse tree
	 */
	void exitLt(InputExpressionParser.LtContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#not}.
	 * @param ctx the parse tree
	 */
	void enterNot(InputExpressionParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#not}.
	 * @param ctx the parse tree
	 */
	void exitNot(InputExpressionParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(InputExpressionParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(InputExpressionParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(InputExpressionParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(InputExpressionParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(InputExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(InputExpressionParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(InputExpressionParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(InputExpressionParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(InputExpressionParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(InputExpressionParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#dereference}.
	 * @param ctx the parse tree
	 */
	void enterDereference(InputExpressionParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#dereference}.
	 * @param ctx the parse tree
	 */
	void exitDereference(InputExpressionParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#plus}.
	 * @param ctx the parse tree
	 */
	void enterPlus(InputExpressionParser.PlusContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#plus}.
	 * @param ctx the parse tree
	 */
	void exitPlus(InputExpressionParser.PlusContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(InputExpressionParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(InputExpressionParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(InputExpressionParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(InputExpressionParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputExpressionParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(InputExpressionParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(InputExpressionParser.StringContext ctx);
}