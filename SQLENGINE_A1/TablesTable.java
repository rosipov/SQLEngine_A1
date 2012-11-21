package sqlengine_a1;
import java.util.*;

public class TablesTable extends CatalogTable {
	public TablesTable(Database db) {
		super(db);
		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
		cols.add(new ColumnDefinition("table_name", "char", 255, false));
		cols.add(new ColumnDefinition("column_count", "int", 10, false));
		this.columns = cols;
	}
	
	public DbResult select(List<String> columnNames, ArbitraryExpression where) throws SqlException {
		if (columnNames == null) {
			columnNames = new ArrayList<String>();
			for (ColumnDefinition col : columns)
				columnNames.add(col.getName());
		}
		
		List<Row> rows = new ArrayList<Row>();
		for (String tableName : database.getTableNames()) {
			List<Data> data = new ArrayList<Data>();
			for (String col : columnNames) {
				if (col.equals("table_name"))
					data.add(new Int(1));
				else if (col.equals("column_count"))
					data.add(new Int(2));
				else
					throw new SqlException("Column " + col + " does not exist in this table");
			}
			rows.add(new Row(this, data));
		}
		
		return new DbResult(columnNames, rows);
	}
}
