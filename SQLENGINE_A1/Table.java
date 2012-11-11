package sqlengine_a1;
import java.util.*;

public class Table {
	private List<ColumnDefinition> columns;
	private List<Row> rows;
	
	public Table(List<ColumnDefinition> columns) {
		this.columns = columns;
	}
	
	public void insertRow(Row row) {
		// TODO validate
		rows.add(row);
	}
	
	public int columnIndex(String columnName) {
		for (int i = 0; i < columns.size(); i++)
			if (columns.get(i).getName().equals(columnName))
				return i;
		return -1;
	}
	
	public DbResult select(List<String> columnNames, ArbitraryExpression where) {
		return new DbResult(null, 0);
	}
}
