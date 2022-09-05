package node;

public class StringNode extends Node{
	private String str;
	public StringNode(String str) {
		this.str = str;
		super.nodeType = NodeType.STRING;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
}
