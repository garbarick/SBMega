package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.adapter.*;

public class Accounts extends ListActivity implements OnAccountsUpdateListener
{
	private Handler handler = new Handler();
	private AccountsAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (manager.getAccountsByType(AccountMega.TYPE).length == 0)
		{
			addNewAccount();
		}
		
		adapter = new AccountsAdapter(this);
		list.setAdapter(adapter);
		
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
            menu.setHeaderTitle(adapter.getItem(info.position).name);
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
				Account account = adapter.getItem(info.position);
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
				adapter.add(account);
			}
		}
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Account account = adapter.getItem(position);
		Intent intent = new Intent(this, Login.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Login.ACCOUNT, account);
		startActivity(intent);
		finish();
	}
}
