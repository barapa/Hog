package util.ast.node;

import util.type.Types;

/**
 * A node representing the Binary Operator terminals in a parse tree.
 * 
 * Note: while not prescribed here, it is assumed that all classes that inherit
 * from BiOpNode have a method evaluate() that returns the evaluation of the
 * expression node. The type of this method should be determined by the type of
 * the node (i.e. multiplicativeExpressionNodes return a number type, and
 * booleanExpressionNodes return a boolean type).
 * 
 * @author sam
 * 
 */
public abstract class BiOpNode extends ExpressionNode {

	public static enum OpType {
		MINUS, PLUS, DIVIDE, TIMES, MOD, LESS, GRTR, LESS_EQL, GRTR_EQL, DBL_EQLS, NOT_EQLS, OR, AND
	}

	protected OpType opType;

	public BiOpNode(OpType type, ExpressionNode left, ExpressionNode right) {
		// note implicitly sets children.parent = this
		this.addChild(left);
		this.addChild(right);
		this.opType = type;
		this.type = Types.getHigherType(left.getType(), right.getType());
	}


	public OpType getOpType() {
		return opType;
	}

	public void setOpType(OpType type) {
		this.opType = type;
	}

	@Override
	public String getName() {
		return opType.toString() + "<" + this.getTypeName() + ">";
	}

}
