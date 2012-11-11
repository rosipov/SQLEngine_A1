package sqlengine_a1.parser;

public class Token {
	public enum Type {
		UNKNOWN,
		KEYWORD, NAME,
		INTEGER, FLOAT, STRING,
		OPEN_PAREN, CLOSE_PAREN, SEMICOLON, COMMA, ASTERISK
	}
	
	public String text;
	public Type type;
	
	public Token(String text) {
		this.text = text;
		this.type = Type.UNKNOWN;
	}
	
	public String toString() {
		return "Token { type=" + type.toString() + ", text=" + text + " }";
	}
}