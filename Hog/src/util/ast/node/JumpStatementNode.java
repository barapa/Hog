package util.ast.node;

import back_end.Visitor;

/**
 * @author ben
 *
 */
public class JumpStatementNode extends StatementNode {
	
	public static enum JumpType {
		CONTINUE, BREAK, RETURN;
	}

	protected JumpType jumpType;
	
	public JumpStatementNode(JumpType jumpType, StatementNode e) {
		this(jumpType);
		this.addChild(e);
	}
	
	public JumpStatementNode(JumpType jumpType) {
		this.jumpType = jumpType;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public String getName() {
		return "JumpStatement: " + jumpType.toString();
	}

	@Override
	public int visitorTest(Visitor v) {
		return 0;
	}
}
