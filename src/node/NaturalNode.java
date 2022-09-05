package node;

public class NaturalNode extends Node{
	private int val;
	public NaturalNode(int val) {
		this.val = val;
		super.nodeType = NodeType.NATURAL;
	}
	
	public int getVal() {
		return this.val;
	}
}
