package node;

public class MainNode extends Node{
	private OtherNode atNode;
	private StmtBlockNode stmtBlockNode;
	
	public MainNode(OtherNode atNode, StmtBlockNode stmtBlockNode) {
		this.nodeType = NodeType.MAIN;
		this.atNode = atNode;
		this.stmtBlockNode = stmtBlockNode;
	}
	
	public OtherNode getAtNode() {
		return this.atNode;
	}
	
	public StmtBlockNode getStmtBlockNode() {
		return this.stmtBlockNode;
	}
}
