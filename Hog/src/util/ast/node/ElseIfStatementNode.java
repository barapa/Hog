package util.ast.node;
/**
 * An ElseIfStatementNode represents an else-if statement
 * and contains the statement if the condition is true, and
 * the statement if the condition is true.
 * 
 * @author ktran
 *
 */
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

}
