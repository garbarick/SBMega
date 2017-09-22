package ru.net.serbis.mega;

import android.content.*;
import android.net.*;
import java.io.*;

public class Utils
{
    public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }
    
    public static void clearOrCreateDir(File dir)
    {
        if (dir.exists())
        {
            clearDir(dir);
        }
        else
        {
            dir.mkdirs();
        }
    }
    
    public static void clearDir(File dir)
    {
        dir.listFiles(
            new FileFilter()
            {
                public boolean accept(File file)
                {
                    if (file.isDirectory())
                    {
                        clearDir(file);
                    }
                    delete(file);
                    return false;    
                }
            }
        );
    }
    
    private static void delete(File file)
    {
        try
        {
            file.delete();
        }
        catch (Throwable ignored)
        {
        }
    }
	
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return manager.getActiveNetworkInfo() != null;
	}
}
