package node;

import java.util.ArrayList;

public class StmtBlockNode extends Node{
	public StmtBlockNode() {
		super.nodeType = NodeType.STMTBLOCK;
	}
	public StmtBlockNode(ArrayList<Node> children) {
		super.children = children;
		super.nodeType = NodeType.STMTBLOCK;
	}
}
