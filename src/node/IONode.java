package node;

public class IONode extends Node{
	private String literal;
	
	public void setOutput() {
		this.literal = "output";
		super.nodeType = NodeType.OUTPUT;
	}
	
	public void setInput() {
		this.literal = "input";
		super.nodeType = NodeType.INPUT;
	}
}
