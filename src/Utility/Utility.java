package Utility;
import java.util.HashMap;


public class Utility {
	public static boolean isNullOrBlank(String str)
    {
    	if (null == str || "".equals(str.trim()))
    	{
    		return true;
    	}
    	return false;
    }
    
}
