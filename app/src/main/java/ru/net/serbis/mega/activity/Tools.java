package ru.net.serbis.mega.activity;

import android.app.*;
import android.view.*;
import android.widget.*;

public class Tools
{
	public static <T extends View> T findView(Activity activity, int id)
    {
        return (T) activity.findViewById(id);
    }
	
	public static String getEditText(Activity activity, int id)
    {
        EditText text = findView(activity, id);
        return text.getText().toString();
    }

    public static void hide(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setVisibility(View.GONE);
    }

    public static void show(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setVisibility(View.VISIBLE);
    }
	
	public static void enable(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setEnabled(true);
    }

    public static void disable(Activity activity, int id)
    {
        View view = findView(activity, id);
        view.setEnabled(false);
    }
}
