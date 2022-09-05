package node;

public class VarDecNode extends Node{
	private VarDecListNode vdlNode;
	
	public VarDecNode() {
		super.nodeType = NodeType.VARDEC;
	}

	public VarDecListNode getVdlNode() {
		return vdlNode;
	}

	public void setVdlNode(VarDecListNode vdlNode) {
		this.vdlNode = vdlNode;
	}
}
