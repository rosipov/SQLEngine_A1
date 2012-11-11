package sqlengine_a1;

public class Int extends Data
{
	private int data;
	private int length;
	
	//create an int type with the data, and the length in digits of that data
	public Int(int data, int length)
	{
		this.data = data;
		this.length = length;
	}
	
	/**
	 * @return the data
	 */
	public int getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(int data)
	{
		this.data = data;
	}

	/**
	 * @return the length
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/*
	 * if the length in digits of the data is longer than the length specified
	 * return false
	 * else return true
	 */
	public boolean isValid()
	{
		String testData = "" + data;
		return !(testData.length()>length);
	}
	
	public String toString()
	{
		String output = "" + data;
		if(output.length()<=length)
		{
			int realLength = output.length();
			for(int i=0; i<(length-realLength); i++)
			{
				output += "_";
			}
		}
		
		return output;
	}
	
}
