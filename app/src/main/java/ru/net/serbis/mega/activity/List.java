package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.app.*;
import android.os.*;
import android.view.*;
import ru.net.serbis.mega.*;

public class List extends Activity
{
	protected AccountManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		manager = AccountManager.get(this);
	}
	
	protected <T extends View> T findView(int id)
    {
        return (T) findViewById(id);
    }
}
