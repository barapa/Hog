package util.ast.node;

import java.util.ArrayList;
import java.util.List;

import util.type.Types;
import util.type.Types.Type;

/**
 * A node representing a primitive constant in a parse tree.
 * 
 * @author sam
 */
public class ConstantNode extends ExpressionNode {

	protected String value;
	
	public ConstantNode(Types.Type type, String value) {
		super(null, type);
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
		
	@Override
	public String getName() {
		return "ConstantNode<" + this.getTypeName() + ">";
	}
	
}
