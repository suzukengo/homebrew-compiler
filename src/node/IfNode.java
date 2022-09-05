package node;

public class IfNode extends Node{
	private OperatorNode cond;
	private StmtBlockNode sbNode;
	private StmtBlockNode elseNode;
	
	public IfNode() {
		super.nodeType = NodeType.IF;
	}
	
	public OperatorNode getCond() {
		return this.cond;
	}
	
	public StmtBlockNode getSbNode() {
		return this.sbNode;
	}
	
	public void setCond(OperatorNode cond) {
		this.cond = cond;
	}
	
	public void setSbNode(StmtBlockNode sbNode) {
		this.sbNode = sbNode;
	}

	public StmtBlockNode getElseNode() {
		return elseNode;
	}

	public void setElseNode(StmtBlockNode elseNode) {
		this.elseNode = elseNode;
	}
}
