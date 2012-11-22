package sqlengine_a1;
import java.util.*;

public class CatalogTable extends Table {
	protected Database database;
	
	public CatalogTable(Database db) {
		super(null);
		this.database = db;
	}
	
	public DbResult insert(List<String> columns, List<List<Data>> rows) throws SqlException {
		throw new SqlException("this table is read-only");
	}
	
	public DbResult delete(ArbitraryExpression where) throws SqlException {
		throw new SqlException("this table is read-only");
	}
	
	public DbResult update(List<String> columnNames, List<ArbitraryExpression> values, ArbitraryExpression where) throws SqlException {
		throw new SqlException("this table is read-only");
	}
}
