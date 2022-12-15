package eean_games.main;

//Code copied from http://oboe2uran.hatenablog.com/entry/2018/07/10/221934

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class WarningHider 
{
	public static void hideIlleagalAccessWarning() 
	{
	    try
	    {
	        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
	        theUnsafe.setAccessible(true);
	        Unsafe u = (Unsafe) theUnsafe.get(null);
	        @SuppressWarnings("rawtypes")
			Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
	        Field logger = cls.getDeclaredField("logger");
	        u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
	    }
	    catch(Exception e)
	    {
	        // ignore
	    }
	}
}

