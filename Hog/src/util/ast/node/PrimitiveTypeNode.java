package util.ast.node;

import back_end.Visitor;
import util.type.Types;

/**
 * @author ben, paul, sam
 * 
 */
public class PrimitiveTypeNode extends TypeNode {

	/** localType field which contains Types.Primitive */
	protected Types.Primitive localType;

	/**
	 * Constructor of PrimitiveTypeNode(Types.Primitive type)
	 * 
	 * @param type
	 */
	public PrimitiveTypeNode(Types.Primitive type) {
		localType = type;
	}

	public Types.Primitive getType() {
		return localType;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public int visitorTest(Visitor v) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Method to Return the name getName()
	 * 
	 * @return Returns a string with the node's name
	 */
	@Override
	public String getName() {
		return "Primitive Type: " + localType.toString();
	}

	@Override
	public boolean isBoolean() {
		return localType == Types.Primitive.BOOL;
	}

	@Override
	public boolean isNumeric() {
		return localType == Types.Primitive.INT
				|| localType == Types.Primitive.REAL;
	}

	@Override
	public boolean isText() {
		return localType == Types.Primitive.TEXT;
	}

}
