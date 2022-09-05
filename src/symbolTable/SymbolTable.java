package symbolTable;

import java.util.ArrayList;

public class SymbolTable {
	private SymbolTable outer; //外側の環境
	private ArrayList<TableEntry> table; //変数に関する情報
	
	public SymbolTable() {
		this(null);
	}
	
	public SymbolTable(SymbolTable outer) {
		this.outer = outer;
		this.table = new ArrayList<TableEntry>();
	}
	
	//変数を登録
	public boolean entryIdent(String name) {
		//すでに登録されている。
		if(this.searchNameInthis(name)) {
			System.out.println("Error,"+name+" is already declared.");
			return false;
		}else {
			var entry = new TableEntry(name);
			this.table.add(entry);
			return true;
		}
	}
	
	public boolean setVal(String name,int val) {
		//名前を探す
		if(this.searchNameInthis(name)) {
			for(var entry:table) {
				if(entry.getName().equals(name) ) {
					entry.setVal(val);
					return true;
				}
			}
			
			if(outer != null)
				return outer.setVal(name, val);
			return false;
		}else { //見つからなかったらエラー 
			System.out.println("Error,"+name+" is not declared");
			return false;
		}
	}
	
	public boolean searchNameInthis(String name) {
		for(var entry:table) {
			if(entry.getName().equals(name)) return true;
		}
		
		if(outer != null) {
			return outer.searchNameInthis(name);
		}
		
		return false;
	}

	public SymbolTable getOuter() {
		return outer;
	}

	public void setOuter(SymbolTable outer) {
		this.outer = outer;
	}

	public ArrayList<TableEntry> getEntry() {
		return table;
	}

	public void setEntry(ArrayList<TableEntry> entry) {
		this.table = entry;
	}
	
	
	
}
