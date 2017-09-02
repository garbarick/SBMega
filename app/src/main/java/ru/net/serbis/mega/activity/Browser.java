package ru.net.serbis.mega.activity;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.adapter.*;
import ru.net.serbis.mega.task.*;

public class Browser extends ListActivity<MegaNode> implements BrowserCallback
{
	private App app;
	private MegaApiAndroid megaApi;
	private MegaNode node;
	private BrowserTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		app = (App) getApplication();
		megaApi = app.getMegaApi();
		task = new BrowserTask(megaApi, this);

		adapter = new NodesAdapter(this);
		list.setAdapter(adapter);
		initList();
	}

	@Override
	protected int getOptionsMenu()
	{
		return R.menu.nodes;
	}

	@Override
	protected int getContextMenu()
	{
		return R.menu.node;
	}

	@Override
	protected String getContextMenuHeader(MegaNode node)
	{
		return node.getName();
	}

	@Override
	protected void editMenu(ContextMenu menu)
	{
		menu.findItem(R.id.move_to_rubbish).setVisible(MegaNode.TYPE_RUBBISH != node.getType());
	}

	@Override
	public boolean onItemMenuSelected(int id, final MegaNode node)
	{
        switch (id)
        {
			case R.id.logout:
				megaApi.logout(task);
				return true;

			case R.id.go_to_rubbish:
				this.node = megaApi.getRubbishNode();
				initList();
				return true;

			case R.id.move:
				{
					Intent intent = new Intent(this, Browser.class);
					intent.putExtra(Constants.SELECT_MODE, true);
					intent.putExtra(Constants.PATH, megaApi.getNodePath(node));
					startActivityForResult(intent, 0);
				}
				return true;

			case R.id.move_to_rubbish:
				new SureDialog(
					this,
					R.string.mess_delete_account,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							megaApi.moveNode(node, megaApi.getRubbishNode(), task);
						}
					});
				return true;

			case R.id.download:
				megaApi.startDownload(node, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/", task);
				return true;
        }
		return super.onItemMenuSelected(id, node);
    }

	private void initList()
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();

		if (node == null)
		{
			node = megaApi.getRootNode();
		}
		if (MegaNode.TYPE_ROOT == node.getType())
		{
			adapter.add(megaApi.getRubbishNode());
		}
		setTitle(node.getName());

		adapter.addAll(megaApi.getChildren(node));

		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MegaNode node = adapter.getItem(position);
		if (node.isFolder())
		{	
			this.node = node;
			initList();
		}
	}

	@Override
	public void onBackPressed()
	{
		switch (node.getType())
		{
			case MegaNode.TYPE_RUBBISH:
				node = null;
				initList();
				break;

			case MegaNode.TYPE_ROOT:
				if (selectMode)
				{
					finish();
				}
				else
				{
					megaApi.logout(task);
				}
				break;

			default:
				node = megaApi.getParentNode(node);
				initList();
				break;
		}
	}

	@Override
	public void onError(MegaError error)
	{
		Toast.makeText(this, error.getErrorCode() + ": " + error.getErrorString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLogout()
	{
		Intent intent = new Intent(this, Accounts.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	public void onRequestStart()
	{
		Tools.disable(this, R.id.list);
	}

	@Override
	public void onRequestFinish()
	{
		Tools.enable(this, R.id.list);
		ProgressBar bar = Tools.findView(this, R.id.login_progress);
		bar.setProgress(0);
	}

	@Override
	public void onDownloadFinish(MegaTransfer transfer)
	{
		Toast.makeText(this, transfer.getParentPath() + transfer.getFileName(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMoveFinish()
	{
		int top = list.getTop();
		initList();
		list.setTop(top);
	}

	@Override
	public void progress(int persent)
	{
		ProgressBar bar = Tools.findView(this, R.id.login_progress);
		bar.setProgress(persent);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.ok:
				{
					Intent intent = new Intent(getIntent());
					intent.putExtra(Constants.SELECT_PATH, megaApi.getNodePath(node));
					setResult(RESULT_OK, intent);
					finish();
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (RESULT_OK == resultCode)
        {
			if (intent.getBooleanExtra(Constants.SELECT_MODE, false))
			{
				String path = intent.getStringExtra(Constants.PATH);
				MegaNode node =megaApi.getNodeByPath(path);

				String selectPath = intent.getStringExtra(Constants.SELECT_PATH);
				MegaNode selectNode =megaApi.getNodeByPath(selectPath);

				megaApi.moveNode(node, selectNode, task);
			}
        }
	}
}
