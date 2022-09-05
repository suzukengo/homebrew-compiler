package node;

public class OtherNode extends Node{
	public enum OtherNodeType{
		SEMICOLLON,
		AT,
		LPAREN,
		RPAREN,
		LBLOCK,
		RBLOCK,
	}
	private OtherNodeType type;
	private String literal;
	
	public OtherNode(OtherNodeType type,String literal) {
		super.nodeType = NodeType.OTHER;
		this.type = type;
		this.literal = literal;
	}
	
	public String getLiteral() {
		return this.literal;
	}
}
