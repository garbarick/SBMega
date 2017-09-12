package ru.net.serbis.mega.service.action;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.task.*;

public class GetFile extends Action
{
	public GetFile(App app, Message msg)
	{
		super(app, msg);
	}
	
	@Override
	public void onFetched()
	{
		MegaNode node = megaApi.getNodeByPath(path);
		megaApi.startDownload(
			node, 
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/",
			new BrowserTask(megaApi, this));
	}

	@Override
	public void onDownloadFinish(MegaTransfer transfer)
	{
		sendResult(Constants.FILE, transfer.getParentPath() + transfer.getFileName());
	}
}
