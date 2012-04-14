package util.ast;

import java.util.Iterator;

import util.ast.node.Node;

/**
 * Abstract class for specifying common behavior for ASTs.
 * 
 * @author sam
 * 
 */
public abstract class AbstractSyntaxTree {

	protected Node root;

	protected AbstractSyntaxTree(Node root) {
		this.root = root;
	}
	
	public Iterator<Node> preOrderTraversal() {

		return TreeTraversalBuilder.buildTraversalIterator(root,
				TreeTraversalBuilder.traversalOrder.PREORDER);

	}

	public Iterator<Node> postOrderTraversal() {

		return TreeTraversalBuilder.buildTraversalIterator(root,
				TreeTraversalBuilder.traversalOrder.POSTORDER);

	}
	
	public String toLatex() {
		
		if (root.getChildren() == null || root.getChildren().size() == 0) {
			return "[.{" + root.getName() + "} ]";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\Tree[.{" + root.getName() + "}");
		
		for (Node child : root.getChildren()) {
			sb.append(toLatexAux(child));
		}
		
		sb.append(" ]");
		
		return sb.toString().replaceAll("<", "\\$<\\$").replaceAll(">", "\\$>\\$");
	}
	
	private String toLatexAux(Node node) {
		
		// base case
		if (node.getChildren() == null || node.getChildren().size() == 0) {
			return " [.{" + node.getName() + "} ]";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" [.{" + node.getName() + "}");
		
		for (Node child : node.getChildren()) {
			sb.append(toLatexAux(child));
		}
		
		sb.append(" ]");
		
		return sb.toString();
		
	}

}
