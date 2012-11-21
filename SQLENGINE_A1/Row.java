package sqlengine_a1;
import java.util.*;

public class Row {
	private Table table;
	private List<Data> data;
	
	public Row(Table table, List<Data> data) {
		this.table = table;
		this.data = data;
	}
	
	public Table getTable() { return table; }
	
	public List<Data> getData() { return data; }
	
	public Data getData(String columnName) {
		int index = this.table.columnIndex(columnName);
		return this.data.get(index);
	}
	
	public Data getData(int i) {
		return this.data.get(i);
	}
}