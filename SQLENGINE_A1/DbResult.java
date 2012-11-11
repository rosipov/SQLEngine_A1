package sqlengine_a1;
import java.util.*;

public class DbResult {
	private List<Row> returnedRows;
	private int affectedRowCount;
	
	public DbResult(List<Row> returnedRows, int affectedRowCount) {
		this.returnedRows = returnedRows;
		this.affectedRowCount = affectedRowCount;
	}
}