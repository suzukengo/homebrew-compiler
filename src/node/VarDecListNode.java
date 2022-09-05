package node;

public class VarDecListNode extends Node{
	private String id;
	private VarDecListNode right;
	
	public VarDecListNode() {
		super.nodeType = NodeType.VARDECLIST;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VarDecListNode getRight() {
		return right;
	}

	public void setRight(VarDecListNode right) {
		this.right = right;
	}
}
