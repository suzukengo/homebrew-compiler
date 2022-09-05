package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import token.Token;
import token.Token.TokenType;

public class Lexer {
	private String input; //入力文字列
	private String input_; //エラー出力用
	private int line; //何行目か(エラー出力用)
	private int now;//今何文字目か
	private int start; //あるトークンの始まりが何文字目か
	private char ch ; //現在の文字
	private FileReader freader;
	private BufferedReader br;
	private boolean lineInc = false;
	
	public Lexer(File file) {
		this.line = 0;
		try {
			freader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		br = new BufferedReader(freader);
		
		try {
			input = br.readLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		if(input.length() > 0) {
			ch = input.charAt(0);
		}else {
			ch = 0;
		}
		input_ = input;
	}
	
	private void getChar() {
		
		if(input == null) return; //行が残っていなければ終了
		
		//次の行を読み込む処理
		if(now >= input.length()) { 
			lineInc=true;
			try {
				input = br.readLine(); //次の行を読み込む
				if(input == null ) ch = 0; //行が残っていなかった場合、次の文字はなし
				else if(input.length() == 0) {
					getChar();
					line++;
				}
				else { 
					ch = input.charAt(0);
					now=0;
				}
				
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				
			}
		}else { //行の文字数を超えていない場合、そのまま次の文字
			
			ch = input.charAt(now);
		}
	}
	
	private void nextChar() {
		this.now++;
		getChar();
	}
	
	public int getNow() {
		return this.now;
	}
	
	//空白はスキップ
	public void skipWhite() {
		while(this.ch == ' ' || this.ch == '\t' || this.ch == '\n') {
			this.nextChar();
		}
	}
	
	public Token lex() {
		if(lineInc) {
			lineInc = false;
			line++;
			skipWhite();
		}
		Token token = null;
		String temp = "";
		
		if(input != null)
			input_ = input;
		
		//空白をスキップ
		skipWhite();
		
		//文字の種類ごとで分類
		switch(ch) {
		case '@':
			token = new Token(String.valueOf(ch),TokenType.AT,100,line,now);
			break;
		case '(':
			token = new Token(String.valueOf(ch),TokenType.LPAREN,101,line,now);
			break;
		case ')':
			token = new Token(String.valueOf(ch),TokenType.RPAREN,102,line,now);
			break;
		case '{':
			token = new Token(String.valueOf(ch),TokenType.LBLOCK,103,line,now);
			break;
		case '}':
			token = new Token(String.valueOf(ch),TokenType.RBLOCK,104,line,now);
			break;
		case '<':
			this.nextChar();
			//次の文字が=ならば、 <= となる。
			if(ch == '=') {
				nextChar();
				token = new Token("<=",TokenType.EGR,107,line,now);
			}else {
				token = new Token(String.valueOf(ch),TokenType.GR,106,line,now);
			}
			return token;
		case '>':
			this.nextChar();
			if(ch == '=') {
				nextChar();
				token = new Token(">=",TokenType.ELE,109,line,now);
			}else token = new Token(String.valueOf(ch),TokenType.LE,108,line,now);
			return token;
		case '!':
			this.nextChar();
				if(ch == '=') {
				nextChar();
				token = new Token(">=",TokenType.NEQ,111,line,now);
			}else token = new Token(String.valueOf(ch),TokenType.NOT,110,line,now);
			break;
		case '=':
			token = new Token(String.valueOf(ch), TokenType.EQ,105,line,now);
			break;
		case '+':
			token = new Token(String.valueOf(ch),TokenType.PLUS,112,line,now);
			break;
		case '-':
			token = new Token(String.valueOf(ch),TokenType.MINUS,113,line,now);
			break;
		case '/':
			token = new Token(String.valueOf(ch),TokenType.PAR,114,line,now);
			break;
		case '*':
			token = new Token(String.valueOf(ch),TokenType.ASTER,115,line,now);
			break;
		case '%':
			token = new Token(String.valueOf(ch),TokenType.MOD,116,line,now);
			break;
		case '\"':
			nextChar();
			while(ch != '\"') {
				temp += ch;
				nextChar();
			}
			token = new Token("\""+temp+"\"",TokenType.STRING,202,line,now);
			break;
		case ',':
			token = new Token(String.valueOf(ch),TokenType.COMMA,118,line,now);
			break;
		case ';':
			token = new Token(String.valueOf(ch),TokenType.SEMICOLON,119,line,now);
			break;
		case 0:
			return null;
		case '\n':
			token = new Token("\n",TokenType.EOF);
			break;
		default:
			int start = now;
			//文字ならそれは識別子
			if(Lexer.isChar(ch)) {
				//文字か数字の限り読み進める
				while(Lexer.isChar(ch) || Lexer.isNumber(ch)) { 
					temp += String.valueOf(ch); //1文字ずつ追加
					nextChar();
				}
				//終わったらトークンに
				
				//予約語の検査
				if(Token.getReserved(temp) != null) token = new Token(temp,Token.getReserved(temp),Token.getReservedCode(temp),line,start);
				else token = new Token(temp,TokenType.IDENT,200,line,start);
				return token;
			}
			
			//数字の場合
			if(Lexer.isNumber(ch)) {
				while(Lexer.isNumber(ch)) {
					temp += String.valueOf(ch);
					nextChar();
				}
				token = new Token(temp,TokenType.NATURAL,201,line,start);
				return token;
			}
			token = new Token(String.valueOf(ch),TokenType.ILLEGAL,999,line,start);
			break;
		}
		
		nextChar();
		return token;
	}
	
	public String getLine() {
		return this.input_;
	}
	
	public int getStart() {
		return this.start;
	}
	
	//文字がchar型か
	public static boolean isChar(char ch) {
		return ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z');
	}
	
	//文字が数字か
	public static boolean isNumber(char ch) {
		return '0' <= ch && ch <= '9';
	}
	
	
}
