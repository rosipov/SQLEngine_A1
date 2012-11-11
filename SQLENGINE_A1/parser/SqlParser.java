
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
try { return parseInsertStatement(); } catch (MaybeParseError e) {}
try { return parseSelectStatement(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['InsertStatement', 'SelectStatement'], next token is " + tokens.get(position));
}
public ASTNode parseInsertStatement() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_STATEMENT);
try { parseInsertKeyword(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseIntoKeyword(); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { ASTNode temp = parseName(); if (temp != null) rv.subnodes.put("table-name", temp); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { ASTNode temp = parseMaybeInsertColumnList(); if (temp != null) rv.subnodes.put("columns", temp); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { parseValuesKeyword(); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { ASTNode temp = parseInsertRowList(); if (temp != null) rv.subnodes.put("rows", temp); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { parseSemicolon(); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
return rv;
}
public ASTNode parseMaybeInsertColumnList() throws ParseError {
try { return parseParenthesizedInsertColumnList(); } catch (MaybeParseError e) {}
try { return parseEpsilon(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['ParenthesizedInsertColumnList', 'Epsilon'], next token is " + tokens.get(position));
}
public ASTNode parseParenthesizedInsertColumnList() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.PARENTHESIZED_INSERT_COLUMN_LIST);
try { parseOpenParen(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseColumnList(); if (temp != null) rv.subnodes.put("columns", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseCloseParen(); }
catch (MaybeParseError e) { position = savePos; throw e; }
return rv;
}
public ASTNode parseColumnList() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.COLUMN_LIST);
try { ASTNode temp = parseName(); if (temp != null) rv.subnodes.put("this", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseColumnList(); if (temp != null) rv.subnodes.put("next", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
return rv;
} catch (MaybeParseError e) {}
try { return parseName(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['Name', 'Name'], next token is " + tokens.get(position));
}
public ASTNode parseInsertRowList() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_ROW_LIST);
try { ASTNode temp = parseParenthesizedInsertRow(); if (temp != null) rv.subnodes.put("this", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRowList(); if (temp != null) rv.subnodes.put("next", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
return rv;
} catch (MaybeParseError e) {}
try { return parseParenthesizedInsertRow(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['ParenthesizedInsertRow', 'ParenthesizedInsertRow'], next token is " + tokens.get(position));
}
public ASTNode parseParenthesizedInsertRow() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.PARENTHESIZED_INSERT_ROW);
try { parseOpenParen(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRow(); if (temp != null) rv.subnodes.put("values", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseCloseParen(); }
catch (MaybeParseError e) { position = savePos; throw e; }
return rv;
}
public ASTNode parseInsertRow() throws ParseError {
try {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.INSERT_ROW);
try { ASTNode temp = parseExpression(); if (temp != null) rv.subnodes.put("this", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { parseComma(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseInsertRow(); if (temp != null) rv.subnodes.put("next", temp); }
catch (MaybeParseError e) { position = savePos; throw e; }
return rv;
} catch (MaybeParseError e) {}
try { return parseExpression(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['Expression', 'Expression'], next token is " + tokens.get(position));
}
public ASTNode parseSelectStatement() throws ParseError {
int savePos = position;
ASTNode rv = new ASTNode(ASTNode.Type.SELECT_STATEMENT);
try { parseSelectKeyword(); }
catch (MaybeParseError e) { position = savePos; throw e; }
try { ASTNode temp = parseMaybeColumnSet(); if (temp != null) rv.subnodes.put("columns", temp); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { parseFromKeyword(); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { ASTNode temp = parseName(); if (temp != null) rv.subnodes.put("table-name", temp); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
try { parseSemicolon(); }
catch (MaybeParseError e) { position = savePos; throw new DefiniteParseError(e.getMessage()); }
return rv;
}
public ASTNode parseMaybeColumnSet() throws ParseError {
try { return parseColumnList(); } catch (MaybeParseError e) {}
try { return parseAsterisk(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['ColumnList', 'Asterisk'], next token is " + tokens.get(position));
}
public ASTNode parseExpression() throws ParseError {
try { return parseString(); } catch (MaybeParseError e) {}
try { return parseInteger(); } catch (MaybeParseError e) {}
try { return parseFloat(); } catch (MaybeParseError e) {}
try { return parseName(); } catch (MaybeParseError e) {}
throw new MaybeParseError("expected one of ['String', 'Integer', 'Float', 'Name'], next token is " + tokens.get(position));
}
public ASTNode parseInsertKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("INSERT")) {
position++;
return new ASTNode(ASTNode.Type.INSERT, "INSERT"); }
else throw new MaybeParseError("expected INSERT, got " + t);
}
public ASTNode parseSelectKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("SELECT")) {
position++;
return new ASTNode(ASTNode.Type.SELECT, "SELECT"); }
else throw new MaybeParseError("expected SELECT, got " + t);
}
public ASTNode parseUpdateKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("UPDATE")) {
position++;
return new ASTNode(ASTNode.Type.UPDATE, "UPDATE"); }
else throw new MaybeParseError("expected UPDATE, got " + t);
}
public ASTNode parseDropKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DROP")) {
position++;
return new ASTNode(ASTNode.Type.DROP, "DROP"); }
else throw new MaybeParseError("expected DROP, got " + t);
}
public ASTNode parseDeleteKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DELETE")) {
position++;
return new ASTNode(ASTNode.Type.DELETE, "DELETE"); }
else throw new MaybeParseError("expected DELETE, got " + t);
}
public ASTNode parseCreateKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("CREATE")) {
position++;
return new ASTNode(ASTNode.Type.CREATE, "CREATE"); }
else throw new MaybeParseError("expected CREATE, got " + t);
}
public ASTNode parseIntoKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("INTO")) {
position++;
return new ASTNode(ASTNode.Type.INTO, "INTO"); }
else throw new MaybeParseError("expected INTO, got " + t);
}
public ASTNode parseFromKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("FROM")) {
position++;
return new ASTNode(ASTNode.Type.FROM, "FROM"); }
else throw new MaybeParseError("expected FROM, got " + t);
}
public ASTNode parseValuesKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("VALUES")) {
position++;
return new ASTNode(ASTNode.Type.VALUES, "VALUES"); }
else throw new MaybeParseError("expected VALUES, got " + t);
}
public ASTNode parseTableKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("TABLE")) {
position++;
return new ASTNode(ASTNode.Type.TABLE, "TABLE"); }
else throw new MaybeParseError("expected TABLE, got " + t);
}
public ASTNode parseSaveKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("SAVE")) {
position++;
return new ASTNode(ASTNode.Type.SAVE, "SAVE"); }
else throw new MaybeParseError("expected SAVE, got " + t);
}
public ASTNode parseCommitKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("COMMIT")) {
position++;
return new ASTNode(ASTNode.Type.COMMIT, "COMMIT"); }
else throw new MaybeParseError("expected COMMIT, got " + t);
}
public ASTNode parseLoadKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("LOAD")) {
position++;
return new ASTNode(ASTNode.Type.LOAD, "LOAD"); }
else throw new MaybeParseError("expected LOAD, got " + t);
}
public ASTNode parseDatabaseKeyword() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.KEYWORD && t.text.equals("DATABASE")) {
position++;
return new ASTNode(ASTNode.Type.DATABASE, "DATABASE"); }
else throw new MaybeParseError("expected DATABASE, got " + t);
}
public ASTNode parseSemicolon() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.SEMICOLON) { position++; return null; }
else throw new MaybeParseError("expected Semicolon, got " + t);
}
public ASTNode parseComma() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.COMMA) { position++; return null; }
else throw new MaybeParseError("expected Comma, got " + t);
}
public ASTNode parseOpenParen() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.OPEN_PAREN) { position++; return null; }
else throw new MaybeParseError("expected OpenParen, got " + t);
}
public ASTNode parseCloseParen() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.CLOSE_PAREN) { position++; return null; }
else throw new MaybeParseError("expected CloseParen, got " + t);
}
public ASTNode parseAsterisk() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.ASTERISK) { position++; return null; }
else throw new MaybeParseError("expected Asterisk, got " + t);
}
public ASTNode parseName() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.NAME) { position++; return new ASTNode(ASTNode.Type.NAME, t.text); }
else throw new MaybeParseError("expected name, got " + t);
}
public ASTNode parseInteger() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.INTEGER) { position++; return new ASTNode(ASTNode.Type.INTEGER, Integer.parseInt(t.text)); }
else throw new MaybeParseError("expected integer, got " + t);
}
public ASTNode parseString() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.STRING) { position++; return new ASTNode(ASTNode.Type.STRING, t.text); }
else throw new MaybeParseError("expected string, got " + t);
}
public ASTNode parseFloat() throws ParseError {
Token t = tokens.get(position);
if (t.type == Token.Type.FLOAT) { position++; return new ASTNode(ASTNode.Type.FLOAT, Float.parseFloat(t.text)); }
else throw new MaybeParseError("expected float, got " + t);
}
public ASTNode parseEpsilon() throws ParseError {
return null;
}
}
