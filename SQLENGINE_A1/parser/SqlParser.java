
package sqlengine_a1.parser;
import java.util.*;

public class SqlParser {
	private Vector<Token> tokens;
	private int position;
	
	public SqlParser(String str) throws ParseError {
		tokens = new SqlTokenizer(str).getAllTokens();
		position = 0;
	}

public ASTNode parseStatement() throws ParseError {
try { return parseInsertStatement(); } catch (ParseError e) {}
throw new ParseError("expected one of ('InsertStatement',), next token is " + tokens.get(position));
}
public ASTNode parseInsertStatement() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_STATEMENT);
try { parseInsertKeyword(); }
catch (ParseError e) { position = savePos; throw e; }
try { parseIntoKeyword(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseName(); if (temp != null) rv.subnodes.put("table-name", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseMaybeInsertColumnList(); if (temp != null) rv.subnodes.put("columns", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseValuesKeyword(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRowList(); if (temp != null) rv.subnodes.put("rows", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseSemicolon(); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
}
public ASTNode parseMaybeInsertColumnList() throws ParseError {
try { return parseParenthesizedInsertColumnList(); } catch (ParseError e) {}
try { return parseEpsilon(); } catch (ParseError e) {}
throw new ParseError("expected one of ('ParenthesizedInsertColumnList', 'Epsilon'), next token is " + tokens.get(position));
}
public ASTNode parseParenthesizedInsertColumnList() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.PARENTHESIZED_INSERT_COLUMN_LIST);
try { parseOpenParen(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertColumnList(); if (temp != null) rv.subnodes.put("columns", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseCloseParen(); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
}
public ASTNode parseInsertColumnList() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_COLUMN_LIST);
try { ASTNode temp = parseName(); if (temp != null) rv.subnodes.put("this", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertColumnList(); if (temp != null) rv.subnodes.put("next", temp); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
} catch (ParseError e) {}
try { return parseName(); } catch (ParseError e) {}
throw new ParseError("expected one of (<__main__.Sequence instance at 0x0000000001EF9388>, 'Name'), next token is " + tokens.get(position));
}
public ASTNode parseInsertRowList() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_ROW_LIST);
try { ASTNode temp = parseParenthesizedInsertRow(); if (temp != null) rv.subnodes.put("this", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRowList(); if (temp != null) rv.subnodes.put("next", temp); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
} catch (ParseError e) {}
try { return parseParenthesizedInsertRow(); } catch (ParseError e) {}
throw new ParseError("expected one of (<__main__.Sequence instance at 0x0000000001EF9388>, 'ParenthesizedInsertRow'), next token is " + tokens.get(position));
}
public ASTNode parseParenthesizedInsertRow() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.PARENTHESIZED_INSERT_ROW);
try { parseOpenParen(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRow(); if (temp != null) rv.subnodes.put("values", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseCloseParen(); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
}
public ASTNode parseInsertRow() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_ROW);
try { ASTNode temp = parseExpression(); if (temp != null) rv.subnodes.put("this", temp); }
catch (ParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (ParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRow(); if (temp != null) rv.subnodes.put("next", temp); }
catch (ParseError e) { position = savePos; throw e; }
return rv;
} catch (ParseError e) {}
try { return parseExpression(); } catch (ParseError e) {}
throw new ParseError("expected one of (<__main__.Sequence instance at 0x0000000001EF9388>, 'Expression'), next token is " + tokens.get(position));
}
public ASTNode parseExpression() throws ParseError {
try { return parseString(); } catch (ParseError e) {}
try { return parseInteger(); } catch (ParseError e) {}
try { return parseFloat(); } catch (ParseError e) {}
try { return parseName(); } catch (ParseError e) {}
throw new ParseError("expected one of ('String', 'Integer', 'Float', 'Name'), next token is " + tokens.get(position));
}
public ASTNode parseInsertKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("INSERT")) {
position++;
return new ASTNode(ASTNode.Type.INSERT, "INSERT"); }
else throw new ParseError("expected INSERT, got " + t);
}
public ASTNode parseSelectKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("SELECT")) {
position++;
return new ASTNode(ASTNode.Type.SELECT, "SELECT"); }
else throw new ParseError("expected SELECT, got " + t);
}
public ASTNode parseUpdateKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("UPDATE")) {
position++;
return new ASTNode(ASTNode.Type.UPDATE, "UPDATE"); }
else throw new ParseError("expected UPDATE, got " + t);
}
public ASTNode parseDropKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DROP")) {
position++;
return new ASTNode(ASTNode.Type.DROP, "DROP"); }
else throw new ParseError("expected DROP, got " + t);
}
public ASTNode parseDeleteKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DELETE")) {
position++;
return new ASTNode(ASTNode.Type.DELETE, "DELETE"); }
else throw new ParseError("expected DELETE, got " + t);
}
public ASTNode parseCreateKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("CREATE")) {
position++;
return new ASTNode(ASTNode.Type.CREATE, "CREATE"); }
else throw new ParseError("expected CREATE, got " + t);
}
public ASTNode parseIntoKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("INTO")) {
position++;
return new ASTNode(ASTNode.Type.INTO, "INTO"); }
else throw new ParseError("expected INTO, got " + t);
}
public ASTNode parseFromKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("FROM")) {
position++;
return new ASTNode(ASTNode.Type.FROM, "FROM"); }
else throw new ParseError("expected FROM, got " + t);
}
public ASTNode parseValuesKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("VALUES")) {
position++;
return new ASTNode(ASTNode.Type.VALUES, "VALUES"); }
else throw new ParseError("expected VALUES, got " + t);
}
public ASTNode parseTableKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("TABLE")) {
position++;
return new ASTNode(ASTNode.Type.TABLE, "TABLE"); }
else throw new ParseError("expected TABLE, got " + t);
}
public ASTNode parseSaveKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("SAVE")) {
position++;
return new ASTNode(ASTNode.Type.SAVE, "SAVE"); }
else throw new ParseError("expected SAVE, got " + t);
}
public ASTNode parseCommitKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("COMMIT")) {
position++;
return new ASTNode(ASTNode.Type.COMMIT, "COMMIT"); }
else throw new ParseError("expected COMMIT, got " + t);
}
public ASTNode parseLoadKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("LOAD")) {
position++;
return new ASTNode(ASTNode.Type.LOAD, "LOAD"); }
else throw new ParseError("expected LOAD, got " + t);
}
public ASTNode parseDatabaseKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DATABASE")) {
position++;
return new ASTNode(ASTNode.Type.DATABASE, "DATABASE"); }
else throw new ParseError("expected DATABASE, got " + t);
}
public ASTNode parseName() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.NAME) {
position++;
return new ASTNode(ASTNode.Type.NAME, t.text); }
else throw new ParseError("expected name, got " + t);
}
public void parseSemicolon() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.SEMICOLON) { position++; }
else throw new ParseError("expected semicolon, got " + t);
}
public void parseComma() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.COMMA) { position++; }
else throw new ParseError("expected comma, got " + t);
}
public void parseOpenParen() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.OPEN_PAREN) { position++; }
else throw new ParseError("expected comma, got " + t);
}
public void parseCloseParen() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.CLOSE_PAREN) { position++; }
else throw new ParseError("expected comma, got " + t);
}
public ASTNode parseInteger() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.INTEGER) { position++; return new ASTNode(ASTNode.Type.INTEGER, Integer.parseInt(t.text)); }
else throw new ParseError("expected comma, got " + t);
}
public ASTNode parseString() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.STRING) { position++; return new ASTNode(ASTNode.Type.STRING, t.text); }
else throw new ParseError("expected comma, got " + t);
}
public ASTNode parseFloat() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.FLOAT) { position++; return new ASTNode(ASTNode.Type.FLOAT, Float.parseFloat(t.text)); }
else throw new ParseError("expected comma, got " + t);
}
public ASTNode parseEpsilon() throws ParseError {
return null;
}
}
