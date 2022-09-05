package node;

import java.util.ArrayList;

public class Node {
	public enum NodeType{
		MAIN,
		STMT,
		STMTBLOCK,
		VARDEC,
		VARDECLIST,
		OUTPUT,
		INPUT,
		IF,
		REPEAT,
		OPERATOR,
		NATURAL,
		OTHER, IDENT, SUBSITUATIONNODE,STRING,
	}
	
	protected NodeType nodeType;
	protected ArrayList<Node> children = new ArrayList<Node>();
	
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	
	public ArrayList<Node> getChildren(){
		return this.children;
	}
	
	//子ノードを追加
	public void addChildren(Node node) {
		this.children.add(node);
	}
	
	public void setChildren(ArrayList<Node> children ) {
		this.children = children;
	}
	
}
