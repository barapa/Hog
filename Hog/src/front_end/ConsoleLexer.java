package front_end;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.ast.AbstractSyntaxTree;
import util.ast.node.BiOpNode;
import util.ast.node.ExpressionNode;
import util.ast.node.IdNode;
import util.ast.node.MockExpressionNode;
import util.ast.node.MockNode;
import util.ast.node.Node;
import util.ast.node.ProgramNode;
import util.symbol_table.SymbolTable;
import util.type.Types;

import java_cup.parser;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import front_end.*;

import java.util.Iterator;
import java.util.logging.*;

import back_end.SymbolTableVisitor;
import back_end.TypeCheckingVisitor;

/**
 * A console front-end to the Lexer class for dynamically testing the Lexer.
 * 
 * @author sam
 *
 */
@SuppressWarnings("unused")
public class ConsoleLexer {
	
	private final static Logger LOGGER = Logger.getLogger(ConsoleLexer.class.getName());

	/**
	 * @param args
	 * */
	public static void main(String[] args) throws IOException {
		
		
		LOGGER.info("Entering ConsoleLexer main()");
		String filename = "WordCount.hog";
		ProgramNode root = null;
		FileReader fileReader = new FileReader(new File(filename));
	    try {
	        // Parser p = new Parser(new Lexer(System.in));
	    	Parser p = new Parser(new Lexer(fileReader));
	    	root = (ProgramNode) p.parse().value;
	    	
	      }
	      catch (FileNotFoundException e) {
	    	  System.out.println("file not found.");
	      }
	      catch (Exception ex) {
	    	  ex.printStackTrace();
	      }
	    
	    AbstractSyntaxTree ast = new AbstractSyntaxTree(root);
	    root.print();
	    
	    SymbolTableVisitor symTabVisitor = new SymbolTableVisitor(ast);
	    symTabVisitor.walk();
	    SymbolTable.printSymbolTable();
	    System.out.println("\n\n\n");
	    SymbolTable.print();
	    
	    root.print();
	    
	    TypeCheckingVisitor typeCheckVisitor = new TypeCheckingVisitor(ast);
	    typeCheckVisitor.walk();
	    
	    root.print();
	    
	}
}
