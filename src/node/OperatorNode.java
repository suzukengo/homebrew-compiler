package node;

public class OperatorNode extends Node{
	public enum OpType{
		GR, // < 
		EGR, // <=
		LE, //>
		ELE ,// >=
		EQ, //=
		NEQ, //!=
		PLUS, //+
		MINUS, //-
		MUL, //*
		DIV, // /
		MOD, // %
	}
	
	private OpType opType;
	private Node left;
	private Node right;
	
	public OperatorNode() {
		super.nodeType = NodeType.OPERATOR;
	}
	
	public OperatorNode(OpType opType,Node left,Node right) {
		this.nodeType = NodeType.OPERATOR;
		this.opType = opType;
		this.left  = left;
		this.right = right;
	}

	public OpType getOpType() {
		return opType;
	}

	public void setOpType(OpType opType) {
		this.opType = opType;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

}
