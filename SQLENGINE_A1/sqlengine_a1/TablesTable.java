package sqlengine_a1;
import java.util.*;

public class TablesTable extends CatalogTable {
	public TablesTable(Database db) {
		super(db);
		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
		cols.add(new ColumnDefinition("table_name", "char", 255, false));
		cols.add(new ColumnDefinition("column_count", "int", 10, false));
		cols.add(new ColumnDefinition("row_count", "int", 10, false));
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
			Table table = database.getTable(tableName);
			
			Row temp = new Row(this, new ArrayList<Data>());
			temp.getData().add(new Int(1));
			temp.getData().add(new Int(table.getColumns().size()));
			temp.getData().add(new Int(table.getRowCount()));
			if (where != null && !where.evaluate(temp).isTrue())
				continue;
			
			List<Data> data = new ArrayList<Data>();
			for (String col : columnNames) {
				data.add(temp.getData(col));
			}
			rows.add(new Row(this, data));
		}
		
		return new DbResult(columnNames, rows);
	}
	
	public int getRowCount() {
		return database.getTableNames().size();
	}
}
