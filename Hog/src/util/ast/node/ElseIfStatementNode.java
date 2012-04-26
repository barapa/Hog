package util.ast.node;

import back_end.Visitor;

public class ElseIfStatementNode extends SelectionStatementNode {

	protected StatementNode ifCondTrue;
	protected StatementNode ifCondFalse;

	public ElseIfStatementNode(ExpressionNode condition,
			StatementListNode checkNext, ElseIfStatementNode ifCondFalse) {
		super(condition);
		this.addChild(ifCondTrue);
		this.addChild(checkNext);
		this.addChild(ifCondFalse);
	}

	@Override
	public String getName() {
		return "ElseIfStatementNode";
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
