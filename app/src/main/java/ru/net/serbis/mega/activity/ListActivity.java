package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;

public abstract class ListActivity<T> extends Activity implements AdapterView.OnItemClickListener
{
	protected AccountManager manager;
	protected ListView list;
	protected ArrayAdapter<T> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		manager = AccountManager.get(this);
		list = Tools.findView(this, R.id.list);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	}
	
	protected abstract int getOptionsMenu();
	protected abstract int getContextMenu();
	protected abstract String getContextMenuHeader(T item);
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getOptionsMenu(), menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (onItemMenuSelected(item.getItemId(), null))
        {
            return true;
		}
		return super.onOptionsItemSelected(item);
    }
	
    public boolean onItemMenuSelected(int id, T item)
    {
        return false;
    }
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getContextMenuHeader(adapter.getItem(info.position)));
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(getContextMenu(), menu);
        }
    }

	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		if (onItemMenuSelected(item.getItemId(), adapter.getItem(info.position)))
        {
            return true;
		}
		return super.onContextItemSelected(item);
    }
}
