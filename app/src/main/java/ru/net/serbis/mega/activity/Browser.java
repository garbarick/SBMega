package ru.net.serbis.mega.activity;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.adapter.*;

public class Browser extends ListActivity implements MegaRequestListenerInterface
{
	private MegaApiAndroid megaApi;
	private NodesAdapter adapter;
	private MegaNode parentNode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		App app = (App) getApplication();
		megaApi = app.getMegaApi();

		adapter = new NodesAdapter(this);
		list.setAdapter(adapter);
		initList();
	}

	private void initList()
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();

		if (parentNode == null)
		{
			parentNode = megaApi.getRootNode();
		}
		setTitle(parentNode.getName());

		List<MegaNode> nodes = megaApi.getChildren(parentNode);
		adapter.addAll(nodes);

		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MegaNode node = adapter.getItem(position);

		if (node.isFolder())
		{	
			parentNode = node;
			initList();
		}
	}

	@Override
	public void onBackPressed()
	{
		parentNode = megaApi.getParentNode(parentNode);
		if (parentNode != null)
		{
			initList();
		}
		else
		{
			megaApi.logout(this);
		}
	}

	@Override
	public void onRequestStart(MegaApiJava api, MegaRequest request)
	{
	}

	@Override
	public void onRequestUpdate(MegaApiJava api, MegaRequest request)
	{
	}

	@Override
	public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
	{
		if (request.getType() == MegaRequest.TYPE_LOGOUT)
		{
			if (error.getErrorCode() == MegaError.API_OK)
			{
				Intent intent = new Intent(this, Accounts.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			else
			{
				Toast.makeText(this, error.getErrorString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError error)
	{
	}
}
