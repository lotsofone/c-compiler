package Lexer;

import Parser.*;

public class Code_Token {
    public Sign sign;
    public String word;
    public int id;
    public int line,column;
    public boolean legal;
    
    public Code_Token() {
    	// Do nothing
    }
    public Code_Token(Sign sign) {
    	this.sign = sign;
    }
    public Code_Token(Sign sign, String word) {
    	this.sign = sign;
    	this.word = word;
    }
    public Code_Token(Code_Token Token) {
		sign = Token.sign;
		if(Token.word != null)
			word = new String(Token.word);
		id = Token.id;
		line = Token.line;
		column = Token.column;
		legal = Token.legal;
	}
    
	public void print() {
    	System.out.print(sign.toString() + ":" + word + " ");
    }
	@Override
	public String toString() {
		return word;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		final Code_Token tk = (Code_Token)obj;
		if(!tk.word.equals(this.word))
			return false;
		if(!tk.sign.equals(this.sign))
			return false;
		return true;
	}
}
