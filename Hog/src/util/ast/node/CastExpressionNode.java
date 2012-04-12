package util.ast.node;

import util.type.Types.Type;

/**
 * A node representing the CastExpression nonterminal in a parse tree.
 * 
 * @author sam
 *
 */
public class CastExpressionNode extends UnOpNode {

	protected Type castType;
	
	public CastExpressionNode(ExpressionNode node, OpType opType, Type type) {
		super(node, opType);
		this.type = type;
	}
	
	public Type getChildType() {
		throw new UnsupportedOperationException("TODO");
	}
	
	@Override
	public String getName() {
		return "CastExpressionNode<" + this.getTypeName() + ">";
	}

}
