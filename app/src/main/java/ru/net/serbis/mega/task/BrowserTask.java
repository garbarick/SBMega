package ru.net.serbis.mega.task;

import nz.mega.sdk.*;

public class BrowserTask implements MegaRequestListenerInterface, MegaTransferListenerInterface
{
	private MegaApiAndroid megaApi;
    private BrowserCallback callback;

	public BrowserTask(MegaApiAndroid megaApi, BrowserCallback callback)
	{
		this.megaApi = megaApi;
		this.callback = callback;
	}
	
	@Override
	public void onRequestStart(MegaApiJava api, MegaRequest request)
	{
		callback.onRequestStart();
	}

	@Override
	public void onRequestUpdate(MegaApiJava api, MegaRequest request)
	{
	}

	@Override
	public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
	{
		callback.onRequestFinish();
		if (error.getErrorCode() == MegaError.API_OK)
		{
			switch(request.getType())
			{
				case MegaRequest.TYPE_LOGOUT:
					callback.onLogout();
					break;
					
				case MegaRequest.TYPE_MOVE:
					callback.onMoveFinish();
					break;
			}
		}
		else
		{
			callback.onError(error);
		}
	}

	@Override
	public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError error)
	{
	}

	@Override
	public void onTransferStart(MegaApiJava api, MegaTransfer transfer)
	{
		callback.onRequestStart();
	}

	@Override
	public void onTransferFinish(MegaApiJava api, MegaTransfer transfer, MegaError error)
	{
		callback.onRequestFinish();
		if (error.getErrorCode() == MegaError.API_OK)
		{
			switch(transfer.getType())
			{
				case MegaTransfer.TYPE_DOWNLOAD:
					callback.onDownloadFinish(transfer);
					break;
					
				case MegaTransfer.TYPE_UPLOAD:
					callback.onMoveFinish();
					break;
			}
		}
		else
		{
			callback.onError(error);
		}
	}

	@Override
	public void onTransferUpdate(MegaApiJava api, MegaTransfer transfer)
	{
		callback.progress(Tools.getProgress(transfer));         
	}
	
	@Override
	public void onTransferTemporaryError(MegaApiJava api, MegaTransfer transfer, MegaError error)
	{
	}

	@Override
	public boolean onTransferData(MegaApiJava api, MegaTransfer transfer, byte[] buffer)
	{
		return false;
	}
}
