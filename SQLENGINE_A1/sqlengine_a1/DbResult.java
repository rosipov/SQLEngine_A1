package sqlengine_a1;
import java.util.*;

public class DbResult {
	private List<String> columnNames;
	private List<Row> returnedRows;
	private int affectedRowCount;
	
	public DbResult(List<String> columnNames, List<Row> returnedRows) {
		this.columnNames = columnNames;
		this.returnedRows = returnedRows;
	}
	
	public DbResult(int affectedRowCount) {
		this.affectedRowCount = affectedRowCount;
	}
	
	public List<String> getColumnNames() { return columnNames; }
	public List<Row> getReturnedRows() { return returnedRows; }
	public int getAffectedRowCount() { return affectedRowCount; }
}