package util.ast.node;

import java.util.ArrayList;

import back_end.Visitor;
/**
 * A node representing the Unary Operator terminals in a parse tree.
 * 
 * @author Samuel Messing
 *
 */
public class UnOpNode extends ExpressionNode {
	
	/** enum OpType
	 * 
	 * note: NONE means no unary operator applied.
	 * @return UMINUS, NOT, RETURN, INCR, DECR, CAST, NONE 
	 */
	public static enum OpType {
		UMINUS, NOT, INCR, DECR, CAST, NONE
	}
	
	protected OpType opType;
	protected ExpressionNode child;
	
	/** Constructor of upOpNode(OpType opType, ExpressionNode child)
	 * @param opType OpType node representing the operation type
	 * @param child ExpressionNode child 
	 * @return 
	 * 
	 */
	public UnOpNode(OpType opType, ExpressionNode child) {
		this(null, opType, child);
		this.child = child;
	}
	
	/** Constructor of upOpNode(TypeNode type, OpType opType, ExpressionNode child)
	 * @param type TypeNode representing the type of the operation
	 * @param opType OpType node representing the operation type
	 * @param child ExpressionNode child 
	 * @return 
	 * 
	 */
	public UnOpNode(TypeNode type, OpType opType, ExpressionNode child) {
		super(type);
		// note implicitly sets child.parent = this
		this.addChild(child);
		this.child = child;
		this.opType = opType;
		this.child = child;
	}
	
	/** Method to Return the opType getOpType()
	 * @return Returns the opType
	 */
	public OpType getOpType() {
		return opType;
	}

	public ExpressionNode getChildNode() {
		return child;
	}

	/** Method to Return the name getName()
	 * @return Returns a string with the node's name
	 */
	@Override
	public String getName() {
		return id + "-" + opType.toString() + "<" + this.getTypeName() + "> newscope: " + isNewScope();
	}
	
	/** Method to Accept Visitor accept(Visitor v)
	 * @return void
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
	
	/** Method to Test Visitor visitorTest(Visitor v)
	 * @return int
	 */
	@Override
	public int visitorTest(Visitor v){
		v.visit(this);
		return 17;
	}
	
	public ExpressionNode getChild() {
		return this.child;
	}
}
