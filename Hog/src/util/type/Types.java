package util.type;

import util.ast.node.ExpressionNode;
import util.ast.node.TypeNode;

public class Types {

	public static enum Primitive {
		BOOL, TEXT, VOID, INT, REAL
	}

	public static enum Derived {
		LIST, ITER, DICT, MULTISET, SET
	}
	
	public static enum Exception {
		// TODO add exception type
	}

	public static boolean isSameType(ExpressionNode one, ExpressionNode two) {
		throw new UnsupportedOperationException("TODO");
	}

	public static TypeNode getHigherType(TypeNode typeOne, TypeNode typeTwo) {

		throw new UnsupportedOperationException("TODO");

	}

	public static boolean isInt(Primitive type) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isReal(Primitive type) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isBool(Primitive type) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isText(Primitive type) {
		// TODO Auto-generated method stub
		return false;
	}

}
