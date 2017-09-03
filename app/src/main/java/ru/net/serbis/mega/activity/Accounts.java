package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.adapter.*;
import ru.net.serbis.mega.data.*;
import ru.net.serbis.mega.service.*;

public class Accounts extends ListActivity<Account> implements OnAccountsUpdateListener
{
	private Handler handler = new Handler();
    private FilesConnection connection = new FilesConnection();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		adapter = new AccountsAdapter(this);
		list.setAdapter(adapter);
		if (params.selectMode)
		{
			Tools.hide(this, R.id.ok);
		}
	}
	
	private void addNewAccount()
	{
		manager.addAccount(
			Constants.TYPE,
			Constants.TOKEN, 
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
	protected int getOptionsMenu()
	{
		return R.menu.accounts;
	}

	@Override
	protected int getContextMenu()
	{
		return R.menu.account;
	}

	@Override
	protected String getContextMenuHeader(Account account)
	{
		return account.name;
	}

	@Override
	public boolean onItemMenuSelected(int id, final Account account)
	{
        switch (id)
        {
            case R.id.add:
            	addNewAccount();
            	return true;
                
            case R.id.select_path:
                {
                    Intent intent = new Intent(this, Accounts.class);
                    params.setActionSelectAccountPath(intent);
                    startActivityForResult(intent, 0);
                }
                return true;
                
            case R.id.get_files_list:
                getFilesList();
                return true;
			
			case R.id.delete:
				new SureDialog(
					this,
					R.string.mess_delete_account,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							manager.removeAccountExplicitly(account);
						}
					});
				return true;
        }
		return super.onItemMenuSelected(id, account);
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
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, FilesService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (connection.isBound())
        {
            unbindService(connection);
        }
    }

	@Override
	public void onAccountsUpdated(Account[] accounts)
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();
		for(Account account : accounts)
		{
			if (Constants.TYPE.equals(account.type))
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
        if (params.selectMode)
        {
            Intent intent = new Intent(this, Login.class);
            params.setActionSelectPath(intent, account);
            startActivityForResult(intent, 0);
        }
        else
        {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            params.setAccount(intent, account);
            startActivity(intent);
            finish();
        }
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (RESULT_OK == resultCode)
        {
            Params params = new Params(data);
            if (params.selectMode)
            {
                switch(params.action)
                {
                    case Constants.ACTION_SELECT_PATH:
                        Intent intent = new Intent(getIntent());
                        String path = "//sbmega/" + params.account.name + params.selectPath;
                        params.selectPath(intent, path);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                        
                    case Constants.ACTION_SELECT_ACCOUNT_PATH:
                        Toast.makeText(this, params.selectPath, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }
    
    private void getFilesList()
    {
        if (connection.isBound())
        {
            Message msg = Message.obtain(null, Constants.ACTION_GET_FILES_LIST, 0, 0);
            msg.replyTo = new Messenger(
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        Toast.makeText(Accounts.this, msg.getData().getString(Constants.FILES_LIST), Toast.LENGTH_LONG).show();
                    }
                }
            );
            try
            {
                connection.getService().send(msg);
            }
            catch (RemoteException e)
            {
                Log.info(this, e);
            }
        }
        else
        {
            Toast.makeText(this, "no connection...", Toast.LENGTH_LONG).show();    
        }
    }
}
