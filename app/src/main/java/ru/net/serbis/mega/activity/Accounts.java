package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.data.*;

public class Accounts extends List implements OnAccountsUpdateListener, AdapterView.OnItemClickListener
{
	private Handler handler = new Handler();
	private ArrayAdapter<AccountView> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (manager.getAccountsByType(AccountMega.TYPE).length == 0)
		{
			addNewAccount();
		}
		
		ListView list = findView(R.id.list);
		adapter = new ArrayAdapter<AccountView>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
	}
	
	private void addNewAccount()
	{
		manager.addAccount(
			AccountMega.TYPE,
			AccountMega.TOKEN, 
			null,
			null,
			this,
			new AccountManagerCallback<Bundle>()
			{
				@Override
				public void run(AccountManagerFuture<Bundle> future)
				{
					try
					{
						future.getResult();
					}
					catch (Throwable e)
					{
						Log.info(this, e);
					}
				}
			},
			null
		);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accounts, menu);
        return true;
    }
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.newAccount:
            {
            	addNewAccount();
            }
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(adapter.getItem(info.position).getAccount().name);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.account, menu);
        }
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.deleteAccount:
            {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				Account account = adapter.getItem(info.position).getAccount();
				manager.removeAccountExplicitly(account);
            }
            return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		manager.addOnAccountsUpdatedListener(this, handler, true);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		manager.removeOnAccountsUpdatedListener(this);
	}

	@Override
	public void onAccountsUpdated(Account[] accounts)
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();
		for(Account account : accounts)
		{
			if (AccountMega.TYPE.equals(account.type))
			{
				adapter.add(new AccountView(account));
			}
		}
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Account account = adapter.getItem(position).getAccount();
		String pass = manager.getPassword(account);
	}
}
