package ru.net.serbis.mega.task;

import android.os.*;
import nz.mega.sdk.*;

public class FetchTask extends AsyncTask<Void, Void, Void> implements MegaRequestListenerInterface
{
	private MegaApiAndroid megaApi;
	private FetchCallback callback;

	public FetchTask(MegaApiAndroid megaApi, FetchCallback callback)
	{
		this.megaApi = megaApi;
		this.callback = callback;
	}

	@Override
	protected Void doInBackground(Void[] p1)
	{
		megaApi.fetchNodes(this);
		return null;
	}
	
	public void onRequestStart(MegaApiJava api, MegaRequest request)
    {
    }

    public void onRequestUpdate(MegaApiJava api, MegaRequest request)
    {
        switch (request.getType())
        {
            case MegaRequest.TYPE_FETCH_NODES:
				callback.progress(Tools.getProgress(request));
				break;
        }
    }

    public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
    {
		if (error.getErrorCode() == MegaError.API_OK)
		{
        	switch (request.getType())
        	{
            	case MegaRequest.TYPE_FETCH_NODES:
				    callback.onFetched();
            		break;
			}
        }
		else
		{
			callback.onError(error);
		}
    }

    public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError error)
    {
    }
}
