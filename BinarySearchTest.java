import java.io.*;
public class BinarySearchTest
{
    String array[] = new String [370105];

    public static void main (String args[])
    {
	new BinarySearchTest ();
    }


    public BinarySearchTest ()
    {
	readIn ();
	String find = IO.inputString ("Word>> ");
	find=find.trim();
	find=find.toLowerCase();
	System.out.println (binarySearch (find));
    }


    boolean binarySearch (String x)
    {
	int l = 0, r = array.length ;
	while (l <= r)
	{
	    int m = l + (r - l) / 2;

	    // Check if x is present at mid
	    if (array [m].equals (x))
		return true;

	    // If x greater, ignore left half
	    if (array [m].compareTo (x) <= 0)
		l = m + 1;

	    // If x is smaller, ignore right half
	    else
		r = m - 1;
	}
	return false;
}

	public void readIn ()
	{
	    BufferedReader in;
	    try
	    {
		in = new BufferedReader (new FileReader ("dictionary.txt"));
		for (int i = 0 ; i < array.length ; i++)
		{
		    array [i] = in.readLine ().trim ();

		}
		in.close ();

	    }
	    catch (IOException e)
	    {
		System.out.println ("Error opening file " + e);
	    }
	}


	public boolean BinarySearch (String find)
	{
	    int high = array.length;
	    int low = 0;
	    boolean foundit = false;
	    int mid = 0;
	    while (high >= low && !foundit)
	    {
		mid = (high + low) / 2;
		if (array [mid].equals (find))
		    foundit = true;
		else if (find.compareTo (array [mid]) > 0)
		    low = mid + 1;
		else //if (find < array [mid])
		    high = mid - 1;
	    }
	    return foundit;
	}
    }


