package sqlengine_a1;

public class ColumnDefinition {
	private String name;
	private String type;
	private int length;
	private boolean nullable;
	
	public ColumnDefinition(String name, String type, int length, boolean nullable) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.nullable = nullable;
	}
	
	public String getName() { return name; }
	public String getType() { return type; }
	public int getLength() { return length; }
	public boolean isNullable() { return nullable; }
}