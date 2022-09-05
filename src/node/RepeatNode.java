package node;

public class RepeatNode extends Node{
	private StmtBlockNode sbNode; //本体
	private OperatorNode cond; //コンディション
	
	public RepeatNode() {
		super.nodeType = NodeType.REPEAT;
	}
	
	public void setSbNode(StmtBlockNode sbNode) {
		this.sbNode = sbNode;
	}

	public OperatorNode getCond() {
		return cond;
	}

	public void setCond(OperatorNode cond) {
		this.cond = cond;
	}

	public StmtBlockNode getSbNode() {
		return sbNode;
	}
	
}
