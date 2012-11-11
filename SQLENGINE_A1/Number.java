
public class Number
{
	private float data;
	String dataString;
	private int decimal;
	
	public Number(float data, int decimal)
	{
		//super();
		this.data = data;
		this.decimal = decimal;
	}
	
	public boolean isValid()
	{
		//String testString = data + "";
		String testString = Float.toString(data);
		System.out.println(testString);
		return true;
	}
	
	
}
