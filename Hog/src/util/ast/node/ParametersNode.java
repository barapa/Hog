package util.ast.node;

import util.type.Types;
import back_end.Visitor;

/**
 * A node representing the Parameters nonterminal in a parse tree.
 * 
 * @author sam & ben
 *
 */
public class ParametersNode extends ExpressionNode {
	
	protected String identifier;
	
	public ParametersNode(Types.Type type, String identifier) {
		super(type);
		this.identifier = identifier;
	}
	
	// deepest node in this chain is the first parameter in the list
	public ParametersNode(Types.Type type, String identifier, ParametersNode child) {
		super(type);
		this.addChild(child);
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String getName() {
		return "ParemetersNode<" + this.getTypeName() + " " + this.getIdentifier() + ">";
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
	
	@Override
	public int visitorTest(Visitor v){
		v.visit(this);
		return 12;
	}
}
