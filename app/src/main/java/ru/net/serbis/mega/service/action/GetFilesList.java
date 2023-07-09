package ru.net.serbis.mega.service.action;

import android.os.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.task.*;

public class GetFilesList extends Action
{
	public GetFilesList(App app, Message msg)
	{
		super(app, msg);
	}

	@Override
	public void onFetched()
	{
		new FileListTask(megaApi, this).execute(email, path);
	}

    @Override
    public void result(String fileslist)
    {
        sendResult(Constants.FILES_LIST, fileslist);
    }
}
