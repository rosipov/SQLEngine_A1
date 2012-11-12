package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class Database {
	private Map<String, Table> tables;
	
	private Database() {
		tables = new HashMap<String, Table>();
	}
	
	public static Database load(String name) {
		// ...
		return new Database();
	}
	
	public static Database create(String name) {
		// ...
		return new Database();
	}
	
	public void save() {
		// ...
	}
	
	public void createTable(String name, List<ColumnDefinition> columns) throws SqlException {
		if (tables.containsKey(name))
			throw new SqlException("table already exists");
		tables.put(name, new Table(columns));
	}
	
	public Table getTable(String name) {
		return tables.get(name);
	}
}