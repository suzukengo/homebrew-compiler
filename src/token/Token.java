package token;

import java.util.HashMap;

public class Token {
	//字句の種類
	public enum TokenType{
		//予約語
		MAIN, //main
		INPUT,  //input
		OUTPUT, //output
		IF, //if
		ELSE, //else
		AS, //as
		YES, //yes
		OTHER, //other
		NO, //no
		START, //start
		REPEAT, //repeat
		
		//記号
		AT, //@
		LPAREN, //(
		RPAREN, //)
		LBLOCK, //{
		RBLOCK, //}
		EQ, //=
		GR, //<
		LE, //>
		EGR, //<=
		ELE, //>=
		NOT, //!
		NEQ, //!=
		PLUS, //+
		MINUS, //-
		ASTER, //*
		PAR, // /
		MOD, //%
		WQUOTE,// "
		COMMA, //,
		SEMICOLON, //;
		AND, // &&
		OR, //||
		EOF, //\n(改行)
		
		//文字、数字、識別子
		IDENT, //識別子
		NATURAL, //自然数
		STRING, //文字列
		
		//エラー
		ILLEGAL, 
	};
	
	private int code; //整数の内部コード
	private String literal; //トークンのリテラル
	private TokenType type; //トークンのタイプ
	private int line;  //何行目か
	private int start; //何文字目か
	private static HashMap<String,TokenType> reserved = new HashMap<String,TokenType>(); //予約語一覧
	private static HashMap<String,Integer> reservedCode = new HashMap<String,Integer>(); //予約語一覧
	
	public Token(String literal, TokenType type) {
		this.literal = literal;
		this.type = type;
		this.code = -1;
		
		entry();
	}
	
	public Token(String literal, TokenType type,int code) {
		this.literal = literal;
		this.type = type;
		this.code = code;
		
		entry();
		
	}
	
	public Token(String literal,TokenType type,int code,int line,int start) {
		this.literal = literal;
		this.type = type;
		this.code = code;
		this.line = line;
		this.start = start;
		entry();
	}
	
	//予約語の登録
	private void entry() {
		reserved.put("main", TokenType.MAIN);
		reserved.put("input", TokenType.INPUT);
		reserved.put("output", TokenType.OUTPUT);
		reserved.put("if", TokenType.IF);
		reserved.put("else",TokenType.ELSE);
		reserved.put("as", TokenType.AS);
		reserved.put("other", TokenType.OTHER);
		reserved.put("yes", TokenType.YES);
		reserved.put("no", TokenType.NO);
		reserved.put("start", TokenType.START);
		reserved.put("repeat", TokenType.REPEAT);
		
		reservedCode.put("main", 0);
		reservedCode.put("input", 7);
		reservedCode.put("if", 2);
		reservedCode.put("else", 3);
		reservedCode.put("as", 9);
		reservedCode.put("yes", 3);
		reservedCode.put("other", 4);
		reservedCode.put("no", 5);
		reservedCode.put("start", 10);
		reservedCode.put("repeat", 11);
		reservedCode.put("output", 12);
		
	}
	
	public String getLiteral() {
		return literal;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public int getCode() {
		return code;
	}
	
	public static TokenType getReserved(String key) {
		return Token.reserved.get(key);
	}
	
	public static int getReservedCode(String key) {
		return Token.reservedCode.get(key);
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
}
