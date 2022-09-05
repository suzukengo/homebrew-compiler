package codeGen;

import node.IdNode;
import node.IfNode;
import node.MainNode;
import node.NaturalNode;
import node.Node;
import node.Node.NodeType;
import node.OperatorNode;
import node.OperatorNode.OpType;
import node.RepeatNode;
import node.StringNode;
import node.SubsituationNode;
import node.VarDecListNode;
import node.VarDecNode;
import symbolTable.SymbolTable;

public class Generator {
	private String cCode;
	//変数環境
	private SymbolTable t;
	private int n; //インデント下げの回数を保存
	
	private String temp;
	public Generator() {
		this.n = 0;
		cCode = "";
		this.t = new SymbolTable();
	}
	
	public Generator(SymbolTable t) {
		this.n = 0;
		this.t = t;
	}
	
	//C言語に変換する
	public String genClang(Node node) {
		if(node == null) return "";
		printTab();
		String stmtBlock = "";
		switch(node.getNodeType()) {
		case MAIN:
			
			return "#include<stdio.h>\nint main(){\n" + this.genClang(((MainNode)node).getStmtBlockNode()) + "\nreturn 0;\n}";
		case STMTBLOCK:
			//スコープを更新
			this.t =  new SymbolTable(this.t);
			
			//1文1文
			for(var stmt:node.getChildren()) {
				stmtBlock += printTab()+this.genClang(stmt);
			}
			this.t = this.t.getOuter();
			return stmtBlock;
		case STMT:
			//1文1文
			for(var stmt:node.getChildren()) {
				stmtBlock += printTab()+this.genClang(stmt);
			}
			return stmtBlock;
		case OUTPUT:
			if(node.getChildren().get(0).getNodeType() == NodeType.STRING) {
				return "printf(" + this.genClang( node.getChildren().get(0)) + ");\n";
			}else {
				return "printf(\"%d\"," + this.genClang( node.getChildren().get(0)) +");\n";
			}
		case INPUT:
			return "scanf(\"%d\",&"+ this.genClang( node.getChildren().get(0)) + ");\n";
		case IF:
			//条件部とブロック部
			if(((IfNode) node).getElseNode() == null){
				return "if(" + this.genClang( ((IfNode) node).getCond()) + "){\n" + 
						this.genClang(((IfNode) node).getSbNode())+"}\n";
			}else {
				return printTab()+"if(" + this.genClang( ((IfNode) node).getCond()) + "){\n" + 
						this.genClang(((IfNode) node).getSbNode())+"}\nelse{\n" + this.genClang(((IfNode) node).getElseNode())+"}\n";
			}
			
		case REPEAT:
			return "do{\n" +
			this.genClang(((RepeatNode) node).getSbNode())+
			"} while(" + 
			this.genClang( ((RepeatNode) node).getCond()) + ");\n";
		case OPERATOR:
			//括弧を余分に付けることで、優先度を表現
			if(((OperatorNode) node).getLeft() != null && ((OperatorNode) node).getRight() != null)
				return "(" + this.genClang(((OperatorNode) node).getLeft()) + this.convertToSym( ((OperatorNode) node).getOpType()) + this.genClang(((OperatorNode) node).getRight()) + ")";
			else if(((OperatorNode) node).getRight() == null)
				return this.genClang(((OperatorNode) node).getLeft());
		case NATURAL:
			return String.valueOf(((NaturalNode)node).getVal());
		case STRING:
			return ((StringNode) node).getStr();
		case VARDEC:
			temp = this.genClang(((VarDecNode)node).getVdlNode());
			if(temp.length() == 0) return "";
			return "int " + temp + ";\n";
		case VARDECLIST:
			if (t.entryIdent(((VarDecListNode) node).getId())) {
				if(((VarDecListNode) node).getRight() == null) {
					return ((VarDecListNode) node).getId();
				}else {
					if(this.genClang(((VarDecListNode) node).getRight()).length() != 0)
						return ((VarDecListNode) node).getId() + "," + this.genClang(((VarDecListNode) node).getRight());
					else
						return ((VarDecListNode) node).getId();
				}
			}else {
				//なかった場合、エラーメッセージがテーブル側で表示される。
				return "";
			}
		case IDENT:
			//名前があった場合
			if(t.setVal(((IdNode) node).getName(),0))
				return ((IdNode) node).getName();
			else return "0";
		case SUBSITUATIONNODE:
			if(t.setVal(((SubsituationNode) node).getId(),0)) {
				return ((SubsituationNode) node).getId() + " = " + this.genClang(((SubsituationNode) node).getEx())+";\n";
			}else {
				return "";
			}
				
		default:
			break;
		}
		return "";
	}

	public String getcCode() {
		return cCode;
	}

	public void setcCode(String cCode) {
		this.cCode = cCode;
	}
	
	public String convertToSym(OpType opType) {
		switch(opType) {
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		case MUL:
			return "*";
		case DIV:
			return "/";
		case MOD:
			return "%";
		case GR:
			return "<";
		case LE:
			return ">";
		case NEQ:
			return "!=";
		case EQ:
			return "==";
		default:
			break;
		}
		return "";
	}
	
	private String printTab() {
		String temp = "";
		for(int i = 0;i < n;i++) {
			temp += "\t";
		}
		return temp;
	}
}
