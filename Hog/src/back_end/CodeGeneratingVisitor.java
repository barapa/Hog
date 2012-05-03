package back_end;

import util.ast.node.ArgumentsNode;
import util.ast.node.BiOpNode;
import util.ast.node.CatchesNode;
import util.ast.node.ConstantNode;
import util.ast.node.DerivedTypeNode;
import util.ast.node.DictTypeNode;
import util.ast.node.ElseIfStatementNode;
import util.ast.node.ElseStatementNode;
import util.ast.node.ExceptionTypeNode;
import util.ast.node.ExpressionNode;
import util.ast.node.FunctionNode;
import util.ast.node.GuardingStatementNode;
import util.ast.node.IdNode;
import util.ast.node.IfElseStatementNode;
import util.ast.node.IterationStatementNode;
import util.ast.node.JumpStatementNode;
import util.ast.node.MockExpressionNode;
import util.ast.node.MockNode;
import util.ast.node.Node;
import util.ast.node.ParametersNode;
import util.ast.node.PostfixExpressionNode;
import util.ast.node.PrimaryExpressionNode;
import util.ast.node.PrimitiveTypeNode;
import util.ast.node.ProgramNode;
import util.ast.node.RelationalExpressionNode;
import util.ast.node.ReservedWordTypeNode;
import util.ast.node.SectionNode;
import util.ast.node.SectionTypeNode;
import util.ast.node.SelectionStatementNode;
import util.ast.node.StatementListNode;
import util.ast.node.StatementNode;
import util.ast.node.SwitchStatementNode;
import util.ast.node.TypeNode;
import util.ast.node.UnOpNode;

import util.ast.AbstractSyntaxTree;
import util.type.Types;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Visitor class for generating Java source.
 * 
 * This is the third (and final) walk performed after construction of the AST
 * from source. CodeGeneratingVisitor generates a massive String representing
 * the translated Hog program.
 * 
 * 
 * @author kurry, sam
 * 
 */
public class CodeGeneratingVisitor implements Visitor {

	protected final static Logger LOGGER = Logger
			.getLogger(CodeGeneratingVisitor.class.getName());

	protected AbstractSyntaxTree tree;
	protected StringBuilder code;
	protected StringBuilder line;

	public CodeGeneratingVisitor(AbstractSyntaxTree root) {

		this.tree = root;
		this.code = new StringBuilder();
		this.line = new StringBuilder();

	}

	public String getCode() {
		return code.toString();
	}

	@Override
	public void walk() {

		writeHeader();

		// start recursive walk:

		walk(tree.getRoot());

	}

	private void walk(Node node) {

		node.accept(this);

		// base cases (sometimes recursion needs to go through visit methods
		// as with If Else statements).

		boolean baseCase = false;

		if (node instanceof IfElseStatementNode) {
			baseCase = true;
		} else if (node instanceof SectionNode) {
			baseCase = true;
		} else if (node instanceof FunctionNode) {
			baseCase = true;
		} else if (node instanceof JumpStatementNode) {
			baseCase = true;
		} else if (node instanceof StatementNode) {
			baseCase = true;
		} else if (node instanceof StatementListNode) {
			baseCase = true;
		} else if (node.getChildren().isEmpty()) {
			baseCase = true;
		}

		// continue recursion if not base case:

		if (!baseCase) {
			for (Node child : node.getChildren()) {
				walk(child);
			}
		}

	}

	private void writeHeader() {
		LOGGER.fine("Writing header to code");
		code.append("import java.io.IOException;\n");
		code.append("import java.util.*;\n");
		code.append("import org.apache.hadoop.fs.Path;\n");
		code.append("import org.apache.hadoop.conf.*;\n");
		code.append("import org.apache.hadoop.io.*;\n");
		code.append("import org.apache.hadoop.mapred.*;\n");
	}

	private void writeFunction() {
		line.append("\n");
		code.append(line.toString());
		LOGGER.fine("[writeFunction] Writing to java source:\n"
				+ line.toString());/**/
		// reset line
		line = new StringBuilder();
	}

	private void writeBlockEnd() {
		line.append("}\n");
		code.append(line.toString());
		LOGGER.fine("[writeFunctions] Writing to java source:\n"
				+ line.toString());/**/
		// reset line
		line = new StringBuilder();
	}

	private void writeStatement() {
		line.append(";\n");
		code.append(line.toString());
		LOGGER.fine("[writeStatement] Writing to java source:\n"
				+ line.toString());/**/
		// reset line
		line = new StringBuilder();
	}

	@Override
	public void visit(ArgumentsNode node) {
		LOGGER.finer("visit(ArgumentsNode node) called on " + node);

	}

	@Override
	public void visit(BiOpNode node) {
		LOGGER.finer("visit(BiOpNode node) called on " + node);
		walk(node.getLeftNode());
		switch (node.getOpType()) {
		case ASSIGN:
			line.append(" = ");
			break;
		case DBL_EQLS:
			line.append(" == ");
			break;
		case NOT_EQLS:
			line.append(" != ");
			break;
		case PLUS:
			line.append(" + ");
			break;
		case OR:
			line.append(" || ");
			break;
		case TIMES:
			line.append(" * ");
			break;
		case MINUS:
			line.append(" - ");
			break;
		case LESS:
			line.append(" < ");
			break;
		case LESS_EQL:
			line.append(" <= ");
			break;
		case GRTR:
			line.append(" > ");
			break;
		case GRTR_EQL:
			line.append(" >= ");
			break;
		case DIVIDE:
			line.append(" / ");
			break;
		case MOD:
			line.append(" % ");
			break;
		case AND:
			line.append(" && ");
			break;
		}
		walk(node.getRightNode());

	}

	@Override
	public void visit(CatchesNode node) {
		LOGGER.finer("visit(CatchesNode node) called on " + node);

	}

	@Override
	public void visit(ConstantNode node) {
		LOGGER.finer("visit(ConstantNode node) called on " + node);
		line.append(node.toSource());
		Types.Primitive primType = ((PrimitiveTypeNode) node.getType()).getType();
		switch (primType) {
		case INT:
		case BOOL:
		case REAL:
		case TEXT:
				line.append(node.getValue());
		}

	}

	@Override
	public void visit(DerivedTypeNode node) {
		LOGGER.finer("visit(DerivedTypeNode node) called on " + node);

	}

	@Override
	public void visit(DictTypeNode node) {
		LOGGER.finer("visit(DictTypeNode node) called on " + node);

	}

	@Override
	public void visit(ElseIfStatementNode node) {
		LOGGER.finer("visit(ElseIfStatementNode node) called on " + node);
		line.append("} else if ( ");
		line.append(node.getCondition().toSource());
		line.append(" ) {\n");
		walk(node.getIfCondTrue());
		if (node.getIfCondFalse() != null) {
			walk(node.getIfCondFalse());
		}

	}

	@Override
	public void visit(ElseStatementNode node) {
		LOGGER.finer("visit(ElseStatementNode node) called on " + node);

		line.append("} else {\n");
		walk(node.getBlock());
		line.append("}\n");

	}

	@Override
	public void visit(ExceptionTypeNode node) {
		LOGGER.finer("visit(ExceptionTypeNode node) called on " + node);

	}

	@Override
	public void visit(ExpressionNode node) {
		LOGGER.finer("visit(ExpressionNode node) called on " + node);
		// ExpressionNode is to general, so move to a more specific case:
		node.accept(this);
	}

	@Override
	public void visit(FunctionNode node) {
		LOGGER.finer("visit(FunctionNodeNode node) called on " + node);
		line.append("public static " + node.getType().toSource() + " "
				+ node.getIdentifier());
		ParametersNode params = node.getParametersNode();
		line.append("(");
		line.append(params.getType().toSource());
		line.append(" " + params.getIdentifier());
		line.append(")");
		line.append(" {\n");
		walk(node.getInstructions());
		writeFunction();
	}

	@Override
	public void visit(GuardingStatementNode node) {
		LOGGER.finer("visit(GuardingStatementNode node) called on " + node);

	}

	@Override
	public void visit(IdNode node) {
		LOGGER.finer("visit(IdNode node) called on " + node);
		line.append(node.toSource());

	}

	@Override
	public void visit(IfElseStatementNode node) {
		LOGGER.finer("visit(IfElseStatementNode node) called on " + node);
		line.append("if ( ");
		line.append(node.getCondition().toSource());
		line.append(" ) {\n");
		walk(node.getIfCondTrue());
		// check that buffer cleared
		if (!line.toString().equals("")) {
			writeStatement();
		}
		if (node.getCheckNext() != null) {
			walk(node.getCheckNext());
		}
		if (node.getIfCondFalse() != null) {
			walk(node.getIfCondFalse());
		}
		line.append("}\n");
	}

	@Override
	public void visit(IterationStatementNode node) {
		LOGGER.finer("visit(IterationStatementNode node) called on " + node);

		switch (node.getIterationType()) {
		case FOR:
			line.append("for ( ");
			walk(node.getInitial());
			line.append("; ");
			walk(node.getCheck());
			line.append("; ");
			walk(node.getIncrement());
			line.append(" ) {\n");
			walk(node.getBlock());
		case FOREACH:
		case WHILE:
		}

		writeBlockEnd();

	}

	@Override
	public void visit(JumpStatementNode node) {
		LOGGER.finer("visit(JumpStatementNode node) called on " + node);

		switch (node.getJumpType()) {
		case RETURN:
			line.append("return ");
			break;
		case BREAK:
			line.append("break");
			break;
		case CONTINUE:
			line.append("continue");
			break;
		}

		if (node.getExpressionNode() != null) {
			walk(node.getExpressionNode());
		}

		writeStatement();
	}

	@Override
	public void visit(MockNode node) {
		LOGGER.finer("visit(MockNode node) called on " + node);

	}

	@Override
	public void visit(MockExpressionNode node) {
		LOGGER.finer("visit(MockExpressionNode node) called on " + node);

	}

	@Override
	public void visit(Node node) {
		LOGGER.finer("visit(Node node) called on " + node);

	}

	@Override
	public void visit(ParametersNode node) {
		LOGGER.finer("visit(ParametersNode node) called on " + node);

	}

	@Override
	public void visit(PostfixExpressionNode node) {
		LOGGER.finer("visit(PostfixExpressionNode node) called on " + node);

		switch (node.getPostfixType()) {
		case ARRAY_INDEX:
			throw new UnsupportedOperationException(
					"Array Indexes have been removed from the grammar. Something's wrong.");
		case METHOD_NO_PARAMS:
			IdNode objectOfMethod = node.getObjectOfMethod();
			IdNode methodNameNoParam = node.getMethodName();
			line.append(objectOfMethod.getIdentifier() + "."
					+ methodNameNoParam.getIdentifier() + "()");

			break;
		case METHOD_WITH_PARAMS:
			IdNode objectName = node.getObjectName();
			IdNode methodName = node.getMethodName();
			if (node.hasArguments()) {
				ExpressionNode argsList = node.getArgsList();
				line.append(objectName.getIdentifier() + "."
						+ methodName.getIdentifier() + "("
						+ argsList.toSource() + ")");
			}

			break;
		case FUNCTION_CALL:
			IdNode functionName = node.getFunctionName();
			if (node.hasArguments()) {
				ExpressionNode functionArgsList = node.getArgsList();
				if (functionArgsList.hasChildren()) {
					Iterator<Node> l = functionArgsList.getChildren()
							.iterator();
					String args = "";

					while (l.hasNext()) {
						Node n = l.next();
						if (n instanceof ConstantNode) {
							ConstantNode cn = (ConstantNode) n;
							args = args + cn.getValue() + ",";
						} else if (n instanceof IdNode) {
							IdNode idNode = (IdNode) n;
							args = args + idNode.getIdentifier() + ",";
						}
					}
					if (args.charAt(args.length() - 1) == ',') {
						args = args.substring(0, args.length() - 1);
					}
					line.append(functionName.toSource() + "(" + args + ")");
				}
			} else
				line.append(functionName.toSource() + "()");
			break;

		}

	}

	@Override
	public void visit(PrimaryExpressionNode node) {
		LOGGER.finer("visit(PrimaryExpressionNode node) called on " + node);
		System.out.println("PRIMARY EXPRESSION NODE VISITED");
		System.out.println(node.toSource());
		line.append(node.toSource());

	}

	@Override
	public void visit(PrimitiveTypeNode node) {
		LOGGER.finer("visit(PrimitiveTypeNode node) called on " + node);

	}

	@Override
	public void visit(ProgramNode node) {
		LOGGER.finer("visit(ProgramNode node) called on " + node);

	}

	@Override
	public void visit(RelationalExpressionNode node) {
		LOGGER.finer("visit(RelationalNode node) called on " + node);

	}

	@Override
	public void visit(ReservedWordTypeNode node) {
		LOGGER.finer("visit(ReservedWordTypeNode node) called on " + node);

	}

	@Override
	public void visit(SectionNode node) {
		LOGGER.finer("visit(SectionNode node) called on " + node);

		SectionNode.SectionName sectionKind = node.getSectionName();

		switch (sectionKind) {
		case FUNCTIONS:
			line.append("public static class Functions {\n");
			break;
		case MAP:
			line.append("public static class Map extends MapReduceBase implements Mapper");
			walk(node.getSectionTypeNode());
			break;
		case REDUCE:
			line.append("public static class Reduce extends MapReduceBase implements Reducer");
			walk(node.getSectionTypeNode());
			break;
		case MAIN:
			line.append("public static void main(String[] args) throws Exception {\n");
			break;
		}

		walk(node.getBlock());

		writeBlockEnd();

	}

	@Override
	public void visit(SectionTypeNode node) {
		LOGGER.finer("visit(SectionTypeNode node) called on " + node);
		line.append("<"
				+ Types.getHadoopType((PrimitiveTypeNode) node
						.getInputKeyIdNode().getType())
				+ ","
				+ Types.getHadoopType((PrimitiveTypeNode) node
						.getInputValueIdNode().getType())
				+ ","
				+ Types.getHadoopType((PrimitiveTypeNode) node.getReturnKey())
				+ ","
				+ Types.getHadoopType((PrimitiveTypeNode) node.getReturnValue())
				+ ">{\n");
	}

	@Override
	public void visit(SelectionStatementNode node) {
		LOGGER.finer("visit(SelectionStatementNode node) called on " + node);

	}

	@Override
	public void visit(StatementListNode node) {
		LOGGER.finer("visit(StatementListNode node) called on " + node);
		for (Node child : node.getChildren()) {
			child.accept(this);
			writeStatement();
		}
		// writeStatement();

	}

	@Override
	public void visit(StatementNode node) {
		LOGGER.finer("visit(StatementNode node) called on " + node);
		for (Node child : node.getChildren()) {
			walk(child);
		}
	}

	@Override
	public void visit(SwitchStatementNode node) {
		LOGGER.finer("visit(SwitchStatementNode node) called on " + node);
	}

	@Override
	public void visit(TypeNode node) {
		LOGGER.finer("visit(TypeNode node) called on " + node);

	}

	@Override
	public void visit(UnOpNode node) {
		LOGGER.finer("visit(UnOpNode node) called on " + node);

		switch (node.getOpType()) {
		case UMINUS:
			line.append("-");
			walk(node.getChildNode());
			break;
		case NOT:
			line.append("!");
			walk(node.getChildNode());
			break;
		case INCR:
			walk(node.getChildNode());
			line.append("++");
			break;
		case DECR:
			walk(node.getChildNode());
			line.append("--");
			break;
		case CAST:
			throw new UnsupportedOperationException(
					"Cast statements are NOT supported yet!");
		case NONE:
			// none means no unary operator applied.
			walk(node.getChildNode());
			break;
		}

	}

}