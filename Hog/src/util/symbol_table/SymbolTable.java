package util.symbol_table;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import util.ast.node.*;
import util.type.Types;
import util.type.VariableRedefinedException;

/**
 * 
 * @author ben, jason
 *
 */

public class SymbolTable {

	   //protected Map<Name, Identifier> variables;
	   static SymbolTable root = null;
	   static SymbolTable top = root;
	   public Map<String,Symbol> table;
	   public SymbolTable outer;
	   protected List<SymbolTable> children;
	   
	   static Map<Node, SymbolTable> nodeToSymbolTableMap = new HashMap<Node, SymbolTable>();
	   
	   private final static Logger LOGGER = Logger.getLogger(SymbolTable.class.getName());
	   protected String symbolTableName;

	   /*
	    * Note From Wikipedia: Not only do type classes permit multiple type
	    * parameters, they also permit functional dependencies between those
	    * type parameters. That is, a programmer can assert that a given
	    * assignment of some subset of the type parameters uniquely determines
	    * the remaining type parameters. 
	    */

    public SymbolTable(SymbolTable prev){
    	if(root == null){
    		this.table = new HashMap<String, Symbol>();
    		this.outer = null;
    		this.children = new ArrayList<SymbolTable>();
    		SymbolTable.top = this;
    		this.fillReservedTable();
    		root = this;
    	}
   
    	else{
    		this.table = new HashMap<String, Symbol>();
    		this.children = new ArrayList<SymbolTable>();
        	this.outer = prev;
    	}
    }
    
    /**
     * mapNode adds a representative node and its corresponding symbol table to the nodeToSymbolTableMap
     * 
     * @param node is the representative node
     * @throws Exception - when node has already been mapped to a symbol table
     */
    public static void mapNode(Node node) throws Exception {
    	// map argument node to this symbol table
    	if (SymbolTable.nodeToSymbolTableMap.put(node, top) != null) {
    		// if its not null, this node has already been mapped, throw an exception
    		LOGGER.severe("mapping representative node: " + node.getName() + " that has already been mapped to a symbol table.");
    		throw new Exception("Representative Node has already been mapped to a symbol table.");
    	}
    	// name the symbol table
    	top.setName(node.getName());
    }
    
    /**
     * 
     * @return the root of the symbol table
     */
    public static SymbolTable getRootSymbolTable() {
    	return root;
    }
    
    /**
     * 
     * @return the SymbolTable at the top
     */
    public static SymbolTable getTop() {
    	return top;
    }

    //move to symbol table in outer scope
    public static void pop(){
    	top = top.outer;
    }
    
    public void setName(String name) {
    	this.symbolTableName = name;
    }
    
    public String getName() {
    	return this.symbolTableName;
    }
    
    /**
     * Adds a variable to the symbol table. 
     * First checks to make sure it is not a reserved word by
     * looking the name up in the reserved word symbol table and
     * makes sure this table doesn't already have the symbol
     * 
     * @param name
     * @param type
     * @return true for successful puts
     * @throws VariableRedefinedException 
     */
    public static boolean put(String name, Symbol symbol) throws VariableRedefinedException{
    	// if not in reserve table
    	if(!top.isDefinedInScope(name)){
            	top.table.put(name, symbol);
            	return true;
    	}
    	throw new VariableRedefinedException("The varliable " + name + " has already been defined");
    }
    
    public static void push() {
    	// save the parent
    	SymbolTable parent = top;
    	// push the new symbol table
    	top = new SymbolTable(top); 
    	
    	if (parent != null) {
        	// add the child
        	parent.addChild(top);
    	}

      }
    
    public static void putToRootSymbolTable(String name, Symbol symbol) {
    	// save the parent
    	root.table.put(name, symbol);

      }
    
    public void addChild(SymbolTable child) {
    	this.children.add(child);
    }
    
     public static void printSymbolTable() {
    	 Set<Node> nodeMapping = nodeToSymbolTableMap.keySet();
    	 for (Node n: nodeMapping) {
    		 System.out.println("\n\nNode: \"" + n.getName() + "\" maps to symbol table: ");
    		 SymbolTable tempTable = nodeToSymbolTableMap.get(n);
    		 Set<String> symbolSet = tempTable.table.keySet();
    		 for (String s : symbolSet) {
    			 System.out.println("key: " + s + "; value: " + tempTable.get(s).toString());
    		 }
    	 }
     }
     
     /**
 	 * A pretty-printer for the subtree that this is a root of. Code taken from:
 	 * http
 	 * ://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 	 * 
 	 */
 	public static void print() {
 		root.print("", true);
 	}

 	/**
 	 * A pretty printer for the subtree that this is a root of. Code taken from:
 	 * http
 	 * ://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
 	 */
 	private void print(String prefix, boolean isTail) {
 		System.out
 				.println(prefix + (isTail ? "L__ " : "|-- ") + this.getName());
 		if (children != null) {
 			for (int i = 0; i < children.size() - 1; i++) {
 				children.get(i).print(prefix + (isTail ? "    " : "|   "),
 						false);
 			}
 			if (children.size() >= 1) {
 				children.get(children.size() - 1).print(
 						prefix + (isTail ? "    " : "|   "), true);
 			}
 		}
 	}
     


    /**
     * Returns whether the particular symbol is defined in this scope. If it isn't
     * in this direct local scope, we ask the parent scope, but don't traverse to enclosing 
     * blocks.
     * 
     * @param name
     * @return true if the identifier is defined in this scope
     */
    public boolean isDefinedInScope(String name){
       	if(this.table.containsKey(name)){
    		return true;
       	} else if (this.outer == null ){
       		return false;
       	} else {
       		return this.outer.isDefinedInScope(name);
       	}
    }
    
    /**
     * look for a specific identifier in this scope
     * 
     * @param name
     * @return the Symbol if found, null otherwise
     */
    public Symbol get(String name){
       	for(SymbolTable st = this; st != null; st = st.outer){
       		Symbol found = st.table.get(name);
       		if(found != null) return found;
       		//identifier in the id node
       	}
       	return null;
    }
    
    /* 
     * get the first symbol table that this node maps to 
     * keep checking parents to see if they map to symbol table 
     */
    public static SymbolTable getMappedSymbolTable(Node n){
    	LOGGER.fine("getMappedSymbolTable() on node: " + n.toString());
    	if(nodeToSymbolTableMap.containsKey(n)){
    		return nodeToSymbolTableMap.get(n);
    	}
    	
    	LOGGER.fine("after nodeToSymbolTableMap.containsKey and get()");
    	Node tempNode = n;
    	while(tempNode.hasParent()){
    		tempNode = tempNode.getParent();
    		LOGGER.fine("inside while loop for n.hasParent()");
    		if(nodeToSymbolTableMap.containsKey(tempNode)){
    			LOGGER.fine("It maps to " + tempNode.getName());
        		return nodeToSymbolTableMap.get(tempNode);
        	}	
    	}
       	
       	return null;
    }
    
    public static Symbol getSymbolForIdNode(IdNode n){
    	//get relevant symbol table for this node
    	SymbolTable nodeTable = getMappedSymbolTable(n);
    	// a table should always be found
    	if(nodeTable == null){
    		try {
    			
				throw new Exception("No Table Found for Node: " + n.getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
    	}
    	
    	if(!nodeTable.isDefinedInScope(n.getIdentifier())){
    		return null;
    	}
    	else{
    		return nodeTable.get(n.getIdentifier());
    	}
    }
    
    /**
     * This is called to get the symbol of a post fix expression
     * @param p
     * @return
     */
    public static FunctionSymbol getSymbolForPostFixExpressionNode(PostfixExpressionNode p) {
    	// if it is a regular function call, 
    	if(p.isFunction())
    		return (FunctionSymbol) getSymbolForIdNode(p.getFunctionName());
    	// if it is a method call
    	else {
    		return SymbolTable.getSymbolForMethodCall(p);
    	}
    }
    
    /**
     * Gets a function symbol for a method call. If the node is not a method call, 
     * throws an exception
     * @param p
     * @return
     */
    public static FunctionSymbol getSymbolForMethodCall(PostfixExpressionNode p) {
    	if (!p.isMethod())
    		throw new RuntimeException("PostFixExpressionNode must me a method call");
    	
    	String typeName = Types.getLowercaseTypeName(p.getObjectName().getType());
    	String methodToLookup = typeName + "." + p.getMethodName().getIdentifier();
    	
    	return (FunctionSymbol) SymbolTable.getRootSymbolTable().get(methodToLookup);
    	
    	
    }
    
    public void reserveWord(String word){ 
    	ReservedWordTypeNode typeNode = new ReservedWordTypeNode(util.type.Types.Flags.RESERVED_WORD);
		this.table.put(word, new VariableSymbol(typeNode));  
	}
    
    public void reserveFunction(String word, TypeNode returnType, ParametersNode paramNode){ 
		this.table.put(word, new FunctionSymbol(returnType, paramNode));  
		
	}
    
    public void reserveFunction(String word, TypeNode returnType, List<TypeNode> paramList) {
    	this.table.put(word,  new FunctionSymbol(returnType, paramList));
    }
    
    public void reserveFunction(String word, TypeNode returnType){ 
		this.table.put(word, new FunctionSymbol(returnType));  
	}
    
	
    
    public void fillReservedTable(){
    	reserveWord("and");
    	reserveWord("or");
    	reserveWord("if");
    	reserveWord("else");
    	reserveWord("elseif");
    	reserveWord("Map");
    	reserveWord("Reduce");
		reserveWord("Main");
		reserveWord("Functions");
		reserveWord("for");
		reserveWord("not");
		reserveWord("in");
		reserveWord("return");
		reserveWord("break");
		reserveWord("case");
		reserveWord("switch");
		reserveWord("void");
		reserveWord("while");
		reserveWord("for");
		reserveWord("foreach");
		reserveWord("list");
		reserveWord("continue");
		reserveWord("int");
		reserveWord("real");
		reserveWord("bool");
		reserveWord("text");
		reserveWord("tokenize");
		reserveWord("default");
		reserveWord("set");
		reserveWord("dict");
		reserveWord("iter");
		reserveWord("add");
		reserveWord("clear");
		reserveWord("get");
		reserveWord("iterator");
		reserveWord("size");
		reserveWord("sort");
		reserveWord("next");
		reserveWord("hasNext");
		reserveWord("peek");
		reserveWord("contains");
		reserveWord("containsAll");
		reserveWord("iter");
		reserveWord("isEmpty");
		reserveWord("remove");
		reserveWord("removeAll");
		reserveWord("mapReduce");
		
		/* add built-in list methods */
		
		ArrayList<TypeNode> printArguments = new ArrayList<TypeNode>();
		printArguments.add(new PrimitiveTypeNode(Types.Primitive.TEXT));
		
		reserveFunction("print", new PrimitiveTypeNode(Types.Primitive.VOID), printArguments);
		
		// add()
		ArrayList<TypeNode> listAddArguments = new ArrayList<TypeNode>();
		listAddArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("list.add", new PrimitiveTypeNode(Types.Primitive.VOID), listAddArguments);
		
		// clear()
		reserveFunction("list.clear", new PrimitiveTypeNode(Types.Primitive.VOID));

		// get()
		ArrayList<TypeNode> listGetArguments = new ArrayList<TypeNode>();
		listGetArguments.add(new PrimitiveTypeNode(Types.Primitive.INT));
		reserveFunction("list.get", new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE), listGetArguments);
		
		// iterator()
		reserveFunction("list.iterator", new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		
		// size()
		reserveFunction("list.size", new PrimitiveTypeNode(Types.Primitive.INT));
		
		// sort()
		reserveFunction("list.sort", new PrimitiveTypeNode(Types.Primitive.VOID));
		
		/* add built-in iter methods */
		reserveFunction("iter.next", new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("iter.hasNext", new PrimitiveTypeNode(Types.Primitive.BOOL));
		reserveFunction("iter.peek", new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		
		/* add built-in set methods */
		// add
		ArrayList<TypeNode> setAddArguments = new ArrayList<TypeNode>();
		setAddArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("set.add", new PrimitiveTypeNode(Types.Primitive.BOOL), setAddArguments);
		
		// clear
		reserveFunction("set.clear", new PrimitiveTypeNode(Types.Primitive.VOID));
		
		// contains
		ArrayList<TypeNode> setContainsArguments = new ArrayList<TypeNode>();
		setContainsArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("set.contains", new PrimitiveTypeNode(Types.Primitive.BOOL), setContainsArguments);
		
		// containsAll
		ArrayList<TypeNode> setContainsAllArguments = new ArrayList<TypeNode>();
		setContainsAllArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_ENTIRE_TYPE));
		reserveFunction("set.containsAll", new PrimitiveTypeNode(Types.Primitive.BOOL), setContainsAllArguments);
		
		// isEmpty()
		reserveFunction("set.isEmpty", new PrimitiveTypeNode(Types.Primitive.BOOL));
		
		// iterator
		// this returns an Iter derived type node, who's inner node is CHECK.INNER.TYPE
		DerivedTypeNode iteratorReturnNode = new DerivedTypeNode(Types.Derived.ITER, new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("set.iterator", iteratorReturnNode);
		ArrayList<TypeNode> setRemoveArguments = new ArrayList<TypeNode>();
		
		// remove()
		setRemoveArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("set.remove", new PrimitiveTypeNode(Types.Primitive.BOOL), setRemoveArguments);
		
		// removeAll()
		ArrayList<TypeNode> setRemoveAllArguments = new ArrayList<TypeNode>();
		setRemoveAllArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("set.removeAll", new PrimitiveTypeNode(Types.Primitive.BOOL), setRemoveAllArguments);
		
		// size()
		reserveFunction("set.size", new PrimitiveTypeNode(Types.Primitive.INT));
		
		//add mapReduce function to reserved table
		reserveFunction("mapReduce", new PrimitiveTypeNode(Types.Primitive.VOID));

		/**
		 * MULTI-SET NOT IMPLEMENTED YET
		 */
/*		 add built-in multiset methods 
		ArrayList<TypeNode> multiSetAddArguments = new ArrayList<TypeNode>();
		multiSetAddArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("multiSet.add", new PrimitiveTypeNode(Types.Primitive.BOOL), multiSetAddArguments);
		reserveFunction("multiSet.clear", new PrimitiveTypeNode(Types.Primitive.VOID));
		ArrayList<TypeNode> multiSetContainArguments = new ArrayList<TypeNode>();
		multiSetContainArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("multiSet.contains", new PrimitiveTypeNode(Types.Primitive.BOOL), multiSetContainArguments);
		ArrayList<TypeNode> multiSetCountArguments = new ArrayList<TypeNode>();
		multiSetCountArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("multiSet.count", new PrimitiveTypeNode(Types.Primitive.INT), multiSetCountArguments);
		reserveFunction("multiSet.entrySet", new PrimitiveTypeNode(Types.Primitive.CHECK_INNER_TYPE));
		reserveFunction("multiSet.isEmpty", new PrimitiveTypeNode(Types.Primitive.BOOL));
		reserveFunction("multiSet.iterator", new PrimitiveTypeNode(Types.Primitive.CHECK_INNER_TYPE));
		ArrayList<TypeNode> multiSetRemoveAllArguments = new ArrayList<TypeNode>();
		multiSetRemoveAllArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("multiSet.removeAll", new PrimitiveTypeNode(Types.Primitive.BOOL), multiSetRemoveAllArguments);
		ArrayList<TypeNode> multiSetRemoveOneArguments = new ArrayList<TypeNode>();
		multiSetRemoveOneArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("multiSet.removeOne", new PrimitiveTypeNode(Types.Primitive.BOOL), multiSetRemoveOneArguments);
		reserveFunction("multiSet.size", new PrimitiveTypeNode(Types.Primitive.INT));
*/
		/* add built-in text functions */
		// length()
		reserveFunction("text.length", new PrimitiveTypeNode(Types.Primitive.INT));
		
		// replace()
		ArrayList<TypeNode> textReplaceArguments = new ArrayList<TypeNode>();
		textReplaceArguments.add(new PrimitiveTypeNode(Types.Primitive.TEXT));
		reserveFunction("text.replace", new PrimitiveTypeNode(Types.Primitive.TEXT), textReplaceArguments);
		
		// tokenize()
		ArrayList<TypeNode> tokenizeArguments = new ArrayList<TypeNode>();
		tokenizeArguments.add(new PrimitiveTypeNode(Types.Primitive.TEXT));
		reserveFunction("text.tokenize", new DerivedTypeNode(Types.Derived.LIST, new PrimitiveTypeNode(Types.Primitive.TEXT)), tokenizeArguments);
		
		/**
		 * DICT NOT IMPLEMENTED YET
		 */
		/* add built-in dict types */
		// clear()
/*		reserveFunction("dict.clear", new PrimitiveTypeNode(Types.Primitive.VOID));
		ArrayList<TypeNode> dictContainsKeyArguments = new ArrayList<TypeNode>();
		dictContainsKeyArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("dict.containsKey", new PrimitiveTypeNode(Types.Primitive.BOOL), dictContainsKeyArguments);
		ArrayList<TypeNode> dictGetArguments = new ArrayList<TypeNode>();
		dictGetArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("dict.get", new PrimitiveTypeNode(Types.Primitive.CHECK_INNER_TYPE), dictGetArguments);
		reserveFunction("dict.keySet", new PrimitiveTypeNode(Types.Primitive.CHECK_INNER_TYPE));
		ArrayList<TypeNode> dictPutArguments = new ArrayList<TypeNode>();
		dictPutArguments.add(new ReservedWordTypeNode(Types.Flags.CHECK_INNER_TYPE));
		reserveFunction("dict.put", new PrimitiveTypeNode(Types.Primitive.VOID), dictPutArguments);
		reserveFunction("dict.size", new PrimitiveTypeNode(Types.Primitive.INT));
		reserveFunction("dict.reverseDict", new PrimitiveTypeNode(Types.Primitive.CHECK_INNER_TYPE));
*/
    }

	
    
}