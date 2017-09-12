package ru.net.serbis.mega.task;

import nz.mega.sdk.*;
import ru.net.serbis.mega.data.*;

public interface LoginCallback
{
	void onLogin(String user);
	
	void onError(MegaError error);
}
