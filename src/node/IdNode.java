package node;

public class IdNode extends Node{
	private String name;
	
	public IdNode() {
		super.nodeType = NodeType.IDENT;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
