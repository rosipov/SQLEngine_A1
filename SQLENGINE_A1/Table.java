package sqlengine_a1;
import java.util.*;

public class Table {
	protected List<ColumnDefinition> columns;
	private List<Row> rows;
	
	public Table(List<ColumnDefinition> columns) {
		this.columns = columns;
	}
	
	public List<ColumnDefinition> getColumns() { return columns; }
	
	public int columnIndex(String columnName) {
		for (int i = 0; i < columns.size(); i++)
			if (columns.get(i).getName().equals(columnName))
				return i;
		return -1;
	}
	
	public DbResult select(List<String> columnNames, ArbitraryExpression where) throws SqlException {
		if (columnNames == null) {
			columnNames = new ArrayList<String>();
			for (ColumnDefinition col : columns)
				columnNames.add(col.getName());
		}
		return new DbResult(columnNames, new ArrayList<Row>());
	}
	
	public DbResult insert(List<String> columns, List<List<Data>> rows) throws SqlException {
		return null;
	}
	
	public DbResult delete(ArbitraryExpression where) throws SqlException {
		return null;
	}
	
	public DbResult update(List<String> columnNames, List<ArbitraryExpression> values, ArbitraryExpression where) throws SqlException {
		return null;
	}
}
