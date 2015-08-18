// Generated from InputExpression.g4 by ANTLR 4.5.1
package ch03.data.expression.parser.input;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link InputExpressionParser}.
 */
public interface InputExpressionListener extends ParseTreeListener {
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
	 * Enter a parse tree produced by {@link InputExpressionParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(InputExpressionParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputExpressionParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(InputExpressionParser.ListContext ctx);
}