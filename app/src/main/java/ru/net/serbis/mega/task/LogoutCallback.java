package ru.net.serbis.mega.task;

import nz.mega.sdk.*;
import ru.net.serbis.mega.data.*;

public interface LogoutCallback
{
	void onError(MegaError error);

	void onLogout();
}
