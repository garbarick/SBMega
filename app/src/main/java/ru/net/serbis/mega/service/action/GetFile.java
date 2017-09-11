package ru.net.serbis.mega.service.action;
import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.task.*;

public class GetFile extends Action
{
	private String resultFile;
	
	public GetFile(App app, Message msg)
	{
		super(app, msg);
	}
	
	@Override
	public void onFetched(MegaRequestListenerInterface listener)
	{
		MegaNode node = megaApi.getNodeByPath(path);
		megaApi.startDownload(
			node, 
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/",
			new BrowserTask(megaApi, this));
	}
	
	@Override
	public void onLogout()
	{
		sendResult(Constants.FILE, resultFile);
	}

	@Override
	public void onDownloadFinish(MegaTransfer transfer)
	{
		resultFile = transfer.getParentPath() + transfer.getFileName();
		logout();
	}
}
