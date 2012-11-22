package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class Database {
	private Map<String, Table> tables;
	private String name;
	
	
	private Database(String name) {
		this.name = name;
		tables = new HashMap<String, Table>();
	}
	
	public static Database load(String name) throws SqlException {
		FileInputStream sFile = null;
		
		try
		{
			sFile = new FileInputStream(name + ".dat");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectInputStream loader = null;
		try
		{
			loader = new ObjectInputStream(sFile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			return (Database) loader.readObject();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("1");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.out.println("2");
		}
		
		try
		{
			loader.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("3");
		}
		return null;
	}
	
	public static Database create(String name) throws SqlException {

		return new Database(name);
	}
	
	public void save() throws SqlException {
		FileOutputStream sFile = null;
		try
		{
			sFile = new FileOutputStream(name + ".dat");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Could not open file");
		}
		
		ObjectOutputStream saver = null;
		try
		{
			saver = new ObjectOutputStream(sFile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Could not initiate output stream.");
		}
		
		try
		{
			saver.writeObject(this);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Could not save the file.");
		}
		
		try
		{
			saver.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Could not close the saver.");
		}
	}
	
	public static void drop(String name) throws SqlException {
		File file = new File(name+".dat");
		file.delete();
	}
	
	public void createTable(String name, List<ColumnDefinition> columns) throws SqlException {
		if (tables.containsKey(name))
			throw new SqlException("table already exists");
		tables.put(name, new Table(columns));
	}
	
	public void dropTable(String name) throws SqlException {
	}
	
	public List<String> getTableNames() {
		ArrayList<String> rv = new ArrayList<String>();
		rv.add("_tables");
		rv.add("_columns");
		for (String t : tables.keySet())
			rv.add(t);
		return rv;
	}
	
	public Table getTable(String name) {
		if (name.equals("_tables"))
			return new TablesTable(this);
		else if (name.equals("_columns"))
			return new ColumnsTable(this);
		return tables.get(name);
	}
}