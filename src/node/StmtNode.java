package node;

import java.util.ArrayList;

public class StmtNode extends Node{
	public StmtNode(ArrayList<Node> children) {
		super.children = children;
		super.nodeType = NodeType.STMT;
	}
	
	public StmtNode(Node node) {
		super.children.add(node);
		super.nodeType = NodeType.STMT;
	}
}
