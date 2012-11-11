package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class Database {
	private Map<String, Table> tables;
	
	public Database() {
		tables = new HashMap<String, Table>();
	}
	
	public static Database load(String name) {
		// ...
		return null;
	}
	
	public void save() {
		// ...
	}
	
	public void createTable(String name, List<ColumnDefinition> columns) {
		// ...
	}
	
	public DbResult execStatement(ASTNode rootNode) {
		if (rootNode.type == ASTNode.Type.CREATE_TABLE_STATEMENT) {
			String name = rootNode.sub("tablename").stringValue;
			return null;
		}
		
		else return null;
	}
}