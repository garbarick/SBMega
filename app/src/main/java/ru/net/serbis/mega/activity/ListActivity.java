package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;

public abstract class ListActivity<T> extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener
{
	protected AccountManager manager;
	protected ListView list;
	protected ArrayAdapter<T> adapter;
	protected boolean selectMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		manager = AccountManager.get(this);
		list = Tools.findView(this, R.id.list);
		list.setOnItemClickListener(this);
		
		selectMode = getIntent().getBooleanExtra(Constants.SELECT_MODE, false);
		if (selectMode)
		{
			setResult(RESULT_CANCELED);
			Tools.show(this, R.id.actions);
			initActions();
		}
		else
		{
			registerForContextMenu(list);
		}
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
		if (!selectMode)
		{
        	MenuInflater inflater = getMenuInflater();
        	inflater.inflate(getOptionsMenu(), menu);
        	return true;
		}
		return false;
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
			editMenu(menu);
        }
    }
	
	protected void editMenu(ContextMenu menu)
	{
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
	
	private void initActions()
	{
		Button ok = Tools.findView(this, R.id.ok);
		ok.setOnClickListener(this);
		Button cancel = Tools.findView(this, R.id.cancel);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.cancel:
				finish();
				break;
		}
	}
}
