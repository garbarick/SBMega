package ru.net.serbis.mega.task;

import nz.mega.sdk.*;

public interface FetchCallback
{
	void progress(int persent);

	void onError(MegaError error);
	
	void onFetched(MegaRequestListenerInterface listener);
}
