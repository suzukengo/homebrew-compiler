package node;

public class SubsituationNode extends Node{
	private OperatorNode ex;
	private String id;
	
	public SubsituationNode() {
		super.nodeType = NodeType.SUBSITUATIONNODE;
	}

	public OperatorNode getEx() {
		return ex;
	}

	public void setEx(OperatorNode ex) {
		this.ex = ex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
