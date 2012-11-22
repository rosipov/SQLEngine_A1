package sqlengine_a1;
import java.util.*;

public class ColumnsTable extends CatalogTable {
	public ColumnsTable(Database db) {
		super(db);
		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
		cols.add(new ColumnDefinition("table_name", "char", 255, false));
		cols.add(new ColumnDefinition("column_name", "char", 255, false));
		cols.add(new ColumnDefinition("type", "char", 255, false));
		cols.add(new ColumnDefinition("length", "char", 10, false));
		cols.add(new ColumnDefinition("nullable", "int", 1, false));
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
			for (ColumnDefinition column : database.getTable(tableName).getColumns()) {
				Row temp = new Row(this, new ArrayList<Data>());
				temp.getData().add(new Int(1));
				temp.getData().add(new Int(2));
				temp.getData().add(new Int(3));
				temp.getData().add(new Int(column.getLength()));
				temp.getData().add(new Int(column.isNullable()? 1 : 0));
				if (where != null && !where.evaluate(temp).isTrue())
					continue;
				
				List<Data> data = new ArrayList<Data>();
				for (String col : columnNames) {
					data.add(temp.getData(col));
				}
				rows.add(new Row(this, data));
			}
		}
		
		return new DbResult(columnNames, rows);
	}
	
	public int getRowCount() {
		int i = 0;
		for (String tableName : database.getTableNames()) {
			for (ColumnDefinition column : database.getTable(tableName).getColumns()) {
				i++;
			}
		}
		return i;
	}
}
