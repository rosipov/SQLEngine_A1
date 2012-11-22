package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class Database {
	private Map<String, Table> tables;
	
	private Database() {
		tables = new HashMap<String, Table>();
	}
	
	public static Database load(String name) throws SqlException {
		// ...
		return new Database();
	}
	
	public static Database create(String name) throws SqlException {
		// ...
		return new Database();
	}
	
	public void save() throws SqlException {
		// ...
	}
	
	public static void drop(String name) throws SqlException {
		// ...
	}
	
	public void createTable(String name, List<ColumnDefinition> columns) throws SqlException {
		if (tables.containsKey(name))
			throw new SqlException("table already exists");
		tables.put(name, new Table(columns));
	}
	
	public void dropTable(String name) throws SqlException {
	}
	
	public List<String> getTableNames() {
		ArrayList<String> rv = new ArrayList<String>();
		rv.add("_tables");
		rv.add("_columns");
		for (String t : tables.keySet())
			rv.add(t);
		return rv;
	}
	
	public Table getTable(String name) {
		if (name.equals("_tables"))
			return new TablesTable(this);
		else if (name.equals("_columns"))
			return new ColumnsTable(this);
		return tables.get(name);
	}
}