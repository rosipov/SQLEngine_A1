package sqlengine_a1;

public class Data
{
	public int compareTo(Data d)
	{
		if((this instanceof Int) && (d instanceof Int))
		{
			if(((Int)this).getData() > ((Int)d).getData())
			{
				return 1;
			}
			else if(((Int)this).getData() < ((Int)d).getData())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		return 0;
	}
}
