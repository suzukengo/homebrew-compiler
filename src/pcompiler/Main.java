package pcompiler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import codeGen.Generator;
import lexer.Lexer;
import node.IdNode;
import node.IfNode;
import node.MainNode;
import node.NaturalNode;
import node.Node;
import node.OperatorNode;
import node.OtherNode;
import node.RepeatNode;
import node.SubsituationNode;
import node.VarDecListNode;
import node.VarDecNode;
import parser.Parser;
import symbolTable.SymbolTable;
import token.Token;

public class Main {
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		*/
		
		File file = null; //入力ファイル
		FileReader fileReader = null; //ファイル読み出しようのインスタンス
		
		//現在のパスを取得
		Path p1 = Paths.get(""); 
		Path p2 = p1.toAbsolutePath(); //これが現在のパス
		
		file = new File(p2.toString()+"\\bin\\pcompiler\\input.txt"); //ファイルを読み込む
		
		if(!file.exists()) { //ファイルが見つからなかった場合
			System.out.println("ファイルが見つかりません");
			return;
		}
		
		Lexer lexer = new Lexer(file);
		Token token = null;
		Parser parser = new Parser(lexer);
		Node node = null;
		
		node = parser.parse();
		Generator g = new Generator(new SymbolTable());
		String code = g.genClang(node);
		System.out.println( code);
		file = new File(p2.toString()+"\\bin\\pcompiler\\output.c");
		FileWriter fw;
		
		try {
			fw = new FileWriter(file);
			fw.write(code);
			fw.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}
	
	private static void printTree(Node node,int n) {
		if(node == null) return;
		Main.printTab(n);

		System.out.println("<"+node.getNodeType()+">");
		switch(node.getNodeType()) {
		case MAIN:
			Main.printTab(n+1);
			System.out.println("<AT>");
			Main.printTab(n+2);
			System.out.println("literal:\"@\"");
			Main.printTab(n+1);
			System.out.println("</AT>");
			Main.printTree(((MainNode)node).getStmtBlockNode(), n+1);
			Main.printTab(n);
			System.out.println("<MAIN>");
			break;
		case OUTPUT:
		case INPUT:
		case STMTBLOCK:
		case STMT:
			for(var stmt:node.getChildren()) {
				Main.printTree(stmt, n+1);
			}
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		case IF:
			Main.printTab(n+1);
			System.out.println("<COND>");
			Main.printTree( ((IfNode) node).getCond(), n+1);
			Main.printTab(n+1);
			System.out.println("</COND>");
			Main.printTree(((IfNode) node).getSbNode(),n+1);
			
			Main.printTab(n);
			System.out.println("</IF>");
			break;
		case REPEAT:
			Main.printTree(((RepeatNode) node).getSbNode(),n+1);
			Main.printTab(n+1);
			System.out.println("<COND>");
			Main.printTree( ((RepeatNode) node).getCond(), n+1);
			Main.printTab(n+1);
			System.out.println("</COND>");
			Main.printTab(n);
			System.out.println("</REPEAT>");
			break;
		case OPERATOR:
			if(((OperatorNode)node).getLeft() != null) {
				Main.printTree(((OperatorNode) node).getLeft(),n+1);
			}
			Main.printTab(n+1);
			//演算子を印字
			System.out.println("<" + ((OperatorNode)node).getOpType() + ">");
			
			if(((OperatorNode)node).getRight() != null) {
				Main.printTree(((OperatorNode) node).getRight(),n+1);
			}
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		case SUBSITUATIONNODE:
			Main.printTree(((SubsituationNode) node).getEx(),n+1);
			Main.printTab(n+1);
			System.out.println("<id=" + ((SubsituationNode) node).getId() + ">");
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		case NATURAL:
			//数値を印字
			Main.printTab(n+1);
			System.out.println("val:\""+((NaturalNode) node).getVal()+"\"");
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		case IDENT:
			//数値を印字
			Main.printTab(n+1);
			System.out.println("<id=" + ((IdNode) node).getName() +">");
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		case VARDEC:
			Main.printTree(((VarDecNode)node).getVdlNode(),n+1);
			Main.printTab(n);
			System.out.println("</" + node.getNodeType() + ">");
			break;
		case VARDECLIST:
			Main.printTab(n+1);
			System.out.println("<id="+((VarDecListNode) node).getId()+">");
			if(((VarDecListNode) node).getRight() != null) {
				Main.printTree(((VarDecListNode) node).getRight(), n+1);
			}
			Main.printTab(n);
			System.out.println("</" + node.getNodeType() + ">");
			break;
		case OTHER:
			Main.printTab(n+1);
			System.out.println("literal:\""+((OtherNode) node).getLiteral()+"\"");
			Main.printTab(n);
			System.out.println("</"+node.getNodeType()+">");
			break;
		default:
			break;
		
		}
	}
	
	private static void printTab(int n) {
		// TODO 自動生成されたメソッド・スタブ
		for(int i = 0;i < n;i++) {
			System.out.print("	");
		}
	}

	private static void printNode(Node node,int n) {
		for(int i = 0;i < n;i++) {
			System.out.println("");
		}
		System.out.println("<" + node.getNodeType() + ">");
		
		for(int i = 0;i < n;i++) {
			System.out.println("");
		}
		System.out.println("</" + node.getNodeType() + ">");
	}

}
