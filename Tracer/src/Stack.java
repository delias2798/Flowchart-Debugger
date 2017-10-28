
public class Stack
{
	private int store[];
	private int max_len, top;

	
	public Stack()
	{
		store = new int[15]; // default size
		max_len = store.length-1;
		top = -1;
	}
	
	public boolean push(int number)
	{
		if (top == max_len)
			return false;
		top++;
		store[top] = number;
		return true;
	}
	
	public void pop()
	{
		if(top >= 0)
		{
			top--;
		}
		else
		{
			System.out.println("Vacio");
		}
	}
	
	public Object topOf()
	{
		if(top >= 0)
		{
			return store[top];			
		}
		return null;
	}
}
