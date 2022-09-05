package parser;

import java.util.ArrayList;

import lexer.Lexer;
import node.IONode;
import node.IdNode;
import node.IfNode;
import node.MainNode;
import node.NaturalNode;
import node.Node;
import node.OperatorNode;
import node.OperatorNode.OpType;
import node.OtherNode;
import node.OtherNode.OtherNodeType;
import node.RepeatNode;
import node.StmtBlockNode;
import node.StmtNode;
import node.StringNode;
import node.SubsituationNode;
import node.VarDecListNode;
import node.VarDecNode;
import token.Token;
import token.Token.TokenType;

public class Parser {
	private Token token;
	private Token nextToken;
	private Lexer lexer;
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		
		//トークンをセット
		nextToken();
		nextToken();
	}
	
	public Node parse() {
		return this.parseMain();
	}
	
	//メイン関数をパース
	public Node parseMain() {
		MainNode node = null;
		if(token == null) return null;
		if(this.token.getType() != TokenType.AT) {
			this.printParseError(TokenType.AT,token);
			this.nextToken();
			return node;
		}else {
			OtherNode temp = new OtherNode(OtherNodeType.AT, "@");
			this.nextToken();
			
			if(this.token.getType() == TokenType.MAIN) {
				this.nextToken();
				StmtBlockNode stmtBlockNode = (StmtBlockNode) this.parseStmtBlockNode();
				node = new MainNode(temp, stmtBlockNode);
			}else {
				this.printParseError(TokenType.MAIN, token);
				StmtBlockNode stmtBlockNode = (StmtBlockNode) this.parseStmtBlockNode();
				node = new MainNode(temp, stmtBlockNode);
			}
		}
		
		return node;
	}
	
	public StmtBlockNode parseStmtBlockNode() {
		StmtBlockNode node = new StmtBlockNode();
		
		//ブロックの始まりは{
		if(this.token.getType() == TokenType.LBLOCK) {
			node.addChildren(new OtherNode(OtherNodeType.LBLOCK,"{"));
			this.nextToken();
			while(this.token.getType() != TokenType.RBLOCK) {
				node.addChildren(this.parseStmt());
			}
		}
		node.addChildren(new OtherNode(OtherNodeType.RBLOCK,"}"));
		this.nextToken();
		return node;
	}
	
	//文をパース
	public Node parseStmt() {
		StmtNode node = null;
		ArrayList<Node> children = new ArrayList<Node>(); //子ノード
		
		if(this.token.getType() == TokenType.OUTPUT) { //出力文
			Node temp = this.parseOutput();
			node = new StmtNode(temp);
		}else if(this.token.getType() == TokenType.INPUT){
			Node temp = this.parseInput();
			node = new StmtNode(temp);
		}else if(this.token.getType() == TokenType.IF) {
			node = new StmtNode(this.parseIfNode());
		}else if(this.token.getType() == TokenType.START){
			this.nextToken();
			StmtBlockNode sbNode = this.parseStmtBlockNode();
			
			//次は"repeat"
			if(this.token.getType() != TokenType.REPEAT) {
				this.printParseError(TokenType.REPEAT, this.token);
				this.nextToken();
			}else {
				this.nextToken();
			}
			OperatorNode on = (OperatorNode) this.parseCondition();
			var rNode = new RepeatNode();
			
			//子ノードをセット
			rNode.setCond(on);
			rNode.setSbNode(sbNode);
			node = new StmtNode(rNode);
		}else if(this.token.getType() == TokenType.AT){
			var decNode = this.parseVarDecNode();
			node = new StmtNode(decNode);
		}else if(this.token.getType() == TokenType.NATURAL |
				this.token.getType() == TokenType.LPAREN |
				this.token.getType() == TokenType.IDENT ) { //代入文 
			var subNode = new SubsituationNode();
			var expression = this.parseExpression();
			
			subNode.setEx((OperatorNode) expression);
			
			//次はas
			if(this.token.getType() == TokenType.AS) {
				this.nextToken();
			}else {
				this.printParseError(TokenType.AS, token);
				this.nextToken();
			}
			
			//名前を登録
			subNode.setId(this.token.getLiteral());
			this.nextToken();
			
			node = new StmtNode(subNode);
		}
		else {
			System.out.println("ParseErro, "+token.getType() + " is not first of statement.");
			if(token != null) {
				while(token.getType() != TokenType.RBLOCK || token.getType() != TokenType.RPAREN || token.getType() != TokenType.SEMICOLON) {
					this.nextToken();
					if(token == null || this.nextToken == null) return null;
				}
				this.nextToken();
			}else {
				return null;
			}
		}
		
		//文の最後はセミコロン
		if(this.token.getType() == TokenType.SEMICOLON) {
			this.nextToken();
			children.add(new OtherNode(OtherNodeType.SEMICOLLON,";"));
		}else {
			children.add(new OtherNode(OtherNodeType.SEMICOLLON,";")); //セミコロンを挿入
			this.nextToken();
			this.printParseError(TokenType.SEMICOLON, token);//エラーメッセを出力してコンパイル続行
		}
		return node;
	}
	
	public IfNode parseIfNode() {
		IfNode ifNode = new IfNode();
		this.nextToken();
		ifNode.setCond((OperatorNode)this.parseCondition()); //conditionをパース
		ifNode.setSbNode(this.parseStmtBlockNode()); //ブロックをパース
		if(this.token.getType() == TokenType.ELSE) {//elseブロック
			if(this.nextToken.getType() != TokenType.LBLOCK) {
				this.nextToken();
				this.printParseError(TokenType.LBLOCK, token);
				return ifNode;
			}
			this.nextToken();
			
			ifNode.setElseNode(this.parseStmtBlockNode());
			
		}
		return ifNode;
	}
	
	public IONode parseOutput() {
		IONode node = new IONode();
		node.setOutput();
		
		if(this.nextToken.getType() == TokenType.STRING) {
			this.nextToken();
			node.addChildren(this.parseString());
		}else {
			this.nextToken();
			node.addChildren(this.parseExpression()); //式をパースして子ノードに追加
		}
		return node;
	}

	public IONode parseInput() {
		IONode node  = new IONode();
		node.setInput();
		if(nextToken.getType() != TokenType.IDENT) {
			this.printParseError(TokenType.IDENT, nextToken);
			this.nextToken();
			return null;
		}
		this.nextToken();
		node.addChildren(this.parseExpression()); //式をパースして子ノードに追加
		
		return node;
	}
	
	public Node parseCondition() {
		var left = this.parseExpression();
		OperatorNode condNode = new OperatorNode();
		switch(this.token.getType()) {
		case GR:
			condNode.setOpType(OpType.GR);
			break;
		case EGR:
			condNode.setOpType(OpType.EGR);
			break;
		case LE:
			condNode.setOpType(OpType.LE);
			break;
		case ELE:
			condNode.setOpType(OpType.ELE);
			break;
		case EQ:
			condNode.setOpType(OpType.EQ);
			break;
		case NEQ:
			condNode.setOpType(OpType.NEQ);
			break;
		default:
			//to do;
		}
		this.nextToken();
		var right = this.parseExpression();
		condNode.setLeft(left);
		condNode.setRight(right);
		return  condNode;
		
	}
	
	//<expression>::=<term>(("+"|"-") <expression>)*
	public Node parseExpression() {
		// TODO 自動生成されたメソッド・スタブ
		Node node =  this.parseTerm();
		
		//トークンがなければ終了
		if(this.token == null) {
			return node;
		}
		
		//トークンがプラスかマイナスならば、termをパースし続ける
		while(this.token.getType() == TokenType.PLUS || this.token.getType() == TokenType.MINUS) {
			//現在のノードが+,-か
			if(this.token.getType() == TokenType.PLUS) {
				this.nextToken();
				node = new OperatorNode(OpType.PLUS,node, this.parseTerm());
			}else if(this.token.getType() == TokenType.MINUS) {
				this.nextToken();
				node = new OperatorNode(OpType.MINUS,node, this.parseTerm());
			}else {
				break;
			}
			if(this.token == null) break;
			
		}
		return node;
	}
	
	public Node parseTerm() {
		Node node =  this.parseFactor();
		
		if(this.token == null) {
			return node;
		}
		
		while(this.token.getType() == TokenType.ASTER || this.token.getType() == TokenType.PAR || this.token.getType() == TokenType.MOD) {
			//現在のノードが+,-か
			if(this.token.getType() == TokenType.ASTER) {
				this.nextToken();
				node = new OperatorNode(OpType.MUL,node, this.parseFactor());
			}else if(this.token.getType() == TokenType.MINUS) {
				this.nextToken();
				node = new OperatorNode(OpType.DIV ,node, this.parseFactor());
			}else if(this.token.getType() == TokenType.MOD) {
				this.nextToken();
				node = new OperatorNode(OpType.MOD,node,this.parseFactor());
			}else break;
			if(this.token == null) break;
		}
		return node;
	}
	
	public Node parseFactor() {
		Node node = null;
		if(this.token.getType() == TokenType.NATURAL  || this.token.getType() == TokenType.MINUS) {
			node = new OperatorNode();
			((OperatorNode) node).setLeft(parseNatural());
			return node;
		}else if(this.token.getType() == TokenType.LPAREN) {
			this.nextToken();
			while(this.token.getType() != TokenType.RPAREN) {
				node = this.parseExpression();
				if(token == null) break;
			}
			this.nextToken();
			return node;
		} else if(this.token.getType() == TokenType.IDENT){
			node = new OperatorNode();
			var idNode = new IdNode();
			idNode.setName(this.token.getLiteral());
			((OperatorNode) node).setLeft(idNode);
			this.nextToken();
			return node;
		} else {
			this.printParseError(TokenType.NATURAL, token);
		}
		return null;
	}
	
	//自然数と負の数も扱える。
	public Node parseNatural() {
		if(this.token.getType() == TokenType.MINUS && this.nextToken.getType() == TokenType.NATURAL) {
			this.nextToken();
			var node = new NaturalNode(Integer.parseInt("-" + token.getLiteral()));
			this.nextToken();
			return node;
		}
		var node = new NaturalNode(Integer.parseInt(token.getLiteral()));
		this.nextToken();
		return node;
	}
	
	private Node parseString() {
		// TODO 自動生成されたメソッド・スタブ
		StringNode strNode = new StringNode(this.token.getLiteral());
		
		this.nextToken();
		
		return strNode;
	}
	
	public VarDecNode parseVarDecNode() {
		var node = new VarDecNode();
		this.nextToken(); // @を読み飛ばす
		//宣言部分をパース
		var decList = this.parseVarDecListNode();
		node.setVdlNode(decList);
		
		return node;
	}
	
	public VarDecListNode parseVarDecListNode() {
		var decList = new VarDecListNode();
		
		//変数名を保存
		decList.setId(this.token.getLiteral());
		
		this.nextToken();
		
		//コンマが来たら宣言が続く
		if(this.token.getType() == TokenType.COMMA) {
			this.nextToken();
			decList.setRight(this.parseVarDecListNode());
		}
		return decList;
	}

	public boolean isEnd() {
		if(token==null)return true;
		else return false;
	}
	
	//トークンを一つ進める 進めた後の現在のトークンを返す
	public Token nextToken() {
		this.token = nextToken;
		nextToken = lexer.lex();
		
		//トークンが不正ならエラーを出し読み飛ばす
		if(this.token != null && this.token.getType() == TokenType.ILLEGAL) {
			System.out.println("TokenError, \""+this.token.getLiteral()+"\" is ILLEGAL.");
			this.nextToken();
		}
		return this.token;
	}
	
	//構文エラー
	public void printParseError(TokenType expected,Token token) {
		//System.out.println("ParseError! line:"+this.token.getLine()+","+(this.token.getStart())+","+ expected + " is expected."
				//+"but get "+ "\"" + token.getLiteral()+ "\"" + ":" + token.getType());
		if(token == null) {
			System.out.println("ParseError! " + expected + " is expected.");
			System.out.println(this.lexer.getLine());
			System.exit(0);
		}else {
			System.out.println("ParseError! " + expected + " is expected."+"but get "+ "\"" + token.getLiteral()+ "\"" + ":" + token.getType());
			System.out.println(this.lexer.getLine());
		}
		/*
		 for(int i = 0;i < this.token.getStart();i++) {
			System.out.print(" ");
		}
		System.out.println("^");
		*/
		System.exit(0);
	}
	
}
