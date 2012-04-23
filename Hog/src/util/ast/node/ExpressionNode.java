package util.ast.node;

import util.type.Types.Primitive;
import java.util.ArrayList;
import java.util.List;

import back_end.Visitor;

/**
 * An abstract class inherited by all expression classes, specifying type information.
 * 
 * @author sam
 *
 */
public abstract class ExpressionNode extends StatementNode {
	
	protected TypeNode type;
	
	
	public ExpressionNode() {
		this(new ArrayList<Node>());
	}
	
	public ExpressionNode(List<Node> children) {
		this(children, null);
	}
	
	public ExpressionNode(TypeNode type) {
		this(new ArrayList<Node>(), type);
	}
	
	public ExpressionNode(List<Node> children, TypeNode type) {
		super(children);
		this.type = type;
	}
	
	public TypeNode getType() {
		return type;
	}
	
	protected void computeType() {
		throw new UnsupportedOperationException("TODO");
	}
	
	public void setType(TypeNode type) {
		this.type = type;
	}
	
	@Override
	public String getName() {
		return "ExpressionNode<" + this.getTypeName() + ">";
	}
	
	public String getTypeName() {
		if (type != null) {
			return type.toString();
		}
		return "unknown";
	}
	

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public int visitorTest(Visitor v){
		v.visit(this);
		return 5;
	}
}
