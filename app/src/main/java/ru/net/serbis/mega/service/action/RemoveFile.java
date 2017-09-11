package ru.net.serbis.mega.service.action;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.task.*;

public class RemoveFile extends Action
{
	private String result;
	
	public RemoveFile(App app, Message msg)
	{
		super(app, msg);
	}
	
	@Override
	public void onFetched(MegaRequestListenerInterface listener)
	{
		MegaNode node = megaApi.getNodeByPath(path);
		megaApi.moveNode(
			node,
			megaApi.getRubbishNode(),
			new BrowserTask(megaApi, this));
	}
	
	@Override
	public void onLogout()
	{
		sendResult(Constants.RESULT, result);
	}

	@Override
	public void onMoveFinish()
	{
		result = Constants.SUCCESS;
		logout();
	}
}
