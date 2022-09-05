package symbolTable;

public class TableEntry {
	public enum Type{
		INTEGER,
	}
	
	private String name;
	private Type type;
	private int val;
	
	public TableEntry(String name) {
		this.name = name;
		this.type = Type.INTEGER;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
}
