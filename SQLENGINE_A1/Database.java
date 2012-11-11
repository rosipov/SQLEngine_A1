package sqlengine_a1;

public class Database {
	private Map<String, Table> tables;
	
	public Database() {
		tables = new Map<String, Table>();
	}
	
	public static Database load() {
		// ...
		return null;
	}
}