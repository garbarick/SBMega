package ru.net.serbis.mega.task;

import nz.mega.sdk.*;
import ru.net.serbis.mega.data.*;

public interface LoginCallback
{
	void onLogin(Token token, MegaRequestListenerInterface listener);
	
	void progress(int persent);
	
	void onError(MegaError error);
	
	void onFetchNode();
	
	void onLogout(Token token);
}
