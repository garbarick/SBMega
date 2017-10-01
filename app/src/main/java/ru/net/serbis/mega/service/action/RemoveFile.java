package ru.net.serbis.mega.service.action;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.task.*;

public class RemoveFile extends Action
{
	public RemoveFile(App app, Message msg)
	{
		super(app, msg);
	}

	@Override
	public void onFetched()
	{
		MegaNode node = megaApi.getNodeByPath(path);
		if (node != null && node.isFile())
		{
			megaApi.moveNode(
				node,
				megaApi.getRubbishNode(),
				new BrowserTask(megaApi, this));
		}
		else
		{
			sendError(Constants.ERROR_FILE_IS_NOT_FOUND, context.getResources().getString(R.string.error_file_is_not_found));
		}
	}

	@Override
	public void onMoveFinish()
	{
		sendResult(Constants.RESULT, Constants.SUCCESS);
	}
}
