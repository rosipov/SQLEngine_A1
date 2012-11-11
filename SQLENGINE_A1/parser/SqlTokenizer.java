package sqlengine_a1.parser;
import java.util.*;

public class SqlTokenizer {
	private String str;
	private int position;
	private final String[] allKeywords = {
		"INSERT", "SELECT", "UPDATE", "DROP", "DELETE", "CREATE", "INTO", "FROM", "VALUES", "TABLE", "SAVE", "COMMIT", "LOAD", "DATABASE"
	};
	
	public SqlTokenizer(String str) {
		this.str = str;
		this.position = 0;
	}
	
	public Vector<Token> getAllTokens() throws ParseError {
		Vector<Token> rv = new Vector<Token>();
		Token t;
		while ((t = nextToken()) != null)
			rv.add(t);
		return rv;
	}
	
	public Token nextToken() throws ParseError {
		String s = nextTokenAsString();
		if (s == null)
			return null;
		
		Token t = new Token(s);
		if (s.equals("("))
			t.type = Token.Type.OPEN_PAREN;
		else if (s.equals(")"))
			t.type = Token.Type.CLOSE_PAREN;
		else if (s.equals(";"))
			t.type = Token.Type.SEMICOLON;
		else if (s.equals(","))
			t.type = Token.Type.COMMA;
		else if (s.equals("*"))
			t.type = Token.Type.ASTERISK;
		else if (s.charAt(0) == '"' || s.charAt(0) == '\'') {
			t.text = t.text.substring(1, t.text.length() - 1);
			t.type = Token.Type.STRING;
		}
		else if (Character.isDigit(s.charAt(0))) {
			if (t.text.indexOf('.') == -1)
				t.type = Token.Type.INTEGER;
			else
				t.type = Token.Type.FLOAT;
		}
		else {
			t.type = Token.Type.NAME;
			for (int i = 0; i < allKeywords.length; i++) {
				if (allKeywords[i].equalsIgnoreCase(t.text)) {
					t.text = t.text.toUpperCase();
					t.type = Token.Type.KEYWORD;
					break;
				}
			}
		}
		
		return t;
	}
	
	private String nextTokenAsString() throws ParseError {
		while (position < str.length() && Character.isWhitespace(str.charAt(position)))
			position++;
		if (position >= str.length())
			return null;
		
		try {
			char c = str.charAt(position++);
			if (c == '(' || c == ')' || c == ';' || c == ',' || c == '*') {
				return Character.toString(c);
			}
			else if (c == '"') {
				StringBuilder sb = new StringBuilder();
				while ((c = str.charAt(position++)) != '"')
					sb.append(c);
				return '"' + sb.toString() + '"';
			}
			else if (c == '\'') {
				StringBuilder sb = new StringBuilder();
				while ((c = str.charAt(position++)) != '\'')
					sb.append(c);
				return '\'' + sb.toString() + '\'';
			}
			else if (Character.isDigit(c)) {
				StringBuilder sb = new StringBuilder();
				sb.append(c);
				while (true) {
					c = str.charAt(position++);
					if (!Character.isDigit(c) && c != '.') {
						position--;
						break;
					}
					sb.append(c);
				}
				return sb.toString();
			}
			else if (Character.isLetter(c) || c == '_') {
				StringBuilder sb = new StringBuilder();
				sb.append(c);
				while (true) {
					c = str.charAt(position++);
					if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
						position--;
						break;
					}
					sb.append(c);
				}
				return sb.toString();
			}
			else {
				throw new ParseError("unexpected character " + c);
			}
		}
		catch (StringIndexOutOfBoundsException e) {
			throw new ParseError("unexpected EOF in token");
		}
	}
}