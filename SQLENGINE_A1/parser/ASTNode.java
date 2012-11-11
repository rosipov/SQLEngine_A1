package sqlengine_a1.parser;
import java.util.*;

public class ASTNode {
	public enum Type {
		INSERT_STATEMENT, COLUMN_LIST, PARENTHESIZED_INSERT_COLUMN_LIST, INSERT_ROW_LIST, PARENTHESIZED_INSERT_ROW, INSERT_ROW,
		SELECT_STATEMENT,
		SAVE_STATEMENT,
		LOAD_STATEMENT,
		CREATE_DATABASE_STATEMENT,
		DROP_DATABASE_STATEMENT,
		DROP_TABLE_STATEMENT,
		QUIT_STATEMENT,
		CREATE_TABLE_STATEMENT, FIELD_DEF_LIST, FIELD_DEF, COLUMN_LENGTH, COLUMN_NULLABLE, COLUMN_NOT_NULLABLE,
		NAME, INTEGER, STRING, FLOAT,
		INSERT, SELECT, UPDATE, DROP, DELETE, CREATE, INTO, FROM, VALUES, TABLE, SAVE, COMMIT, LOAD, DATABASE, QUIT, NOT, NULL
	}

	public Map<String, ASTNode> subnodes;
	public Type type;
	public String stringValue;
	public int intValue;
	public float floatValue;
	
	public ASTNode(Type type) {
		this.type = type;
		this.subnodes = new HashMap<String, ASTNode>();
	}
	
	public ASTNode(Type type, String text) {
		this(type);
		this.stringValue = text;
	}
	
	public ASTNode(Type type, int i) {
		this(type);
		this.intValue = i;
	}
	
	public ASTNode(Type type, float i) {
		this(type);
		this.floatValue = i;
	}
	
	public String toString() {
		return toString(0, "(root node)");
	}
	
	public String toString(int indent, String nodeName) {
		StringBuilder rv = new StringBuilder();
		for (int i = 0; i < indent; i++)
			rv.append("  ");
		rv.append(nodeName + " = " + type.toString() + " { " + (
			this.type == Type.INTEGER? Integer.toString(this.intValue) :
			this.type == Type.FLOAT? Float.toString(this.floatValue) :
			this.stringValue
		) + " }\n");
		for (String k : subnodes.keySet())
			rv.append(subnodes.get(k).toString(indent + 1, k));
		return rv.toString();
	}
}