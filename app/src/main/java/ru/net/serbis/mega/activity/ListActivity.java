package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;

public class ListActivity extends Activity implements AdapterView.OnItemClickListener
{
	protected AccountManager manager;
	protected ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		manager = AccountManager.get(this);
		list = findView(R.id.list);
		list.setOnItemClickListener(this);
	}
	
	protected <T extends View> T findView(int id)
    {
        return (T) findViewById(id);
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	}
}
