package ru.net.serbis.mega;

import java.io.BufferedReader;

public class Log
{
    public static void info(Object o, BufferedReader input, String prefix) throws Exception
    {
        String line;
        while (input.ready() && (line = input.readLine()) != null)
        {
            info(o, prefix + ":" + line);
        }
    }

    public static void info(Object o, String message)
    {
        android.util.Log.i(o.getClass().getName(), message);
    }

    public static void error(Object o, String message, Throwable e)
    {
        android.util.Log.e(o.getClass().getName(), message, e);
    }
    
    public static void error(Object o, Throwable e)
    {
        android.util.Log.e(o.getClass().getName(), "Error", e);
    }
}
