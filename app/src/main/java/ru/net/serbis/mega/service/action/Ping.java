package ru.net.serbis.mega.service.action;

import android.os.*;
import ru.net.serbis.mega.*;

public class Ping extends Action
{
	public Ping(App app, Message msg)
	{
		super(app, msg);
	}

	@Override
	public void execute()
	{
		sendResult(Constants.RESULT, Constants.SUCCESS);
	}
}
