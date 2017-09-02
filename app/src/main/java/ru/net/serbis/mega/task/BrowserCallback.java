package ru.net.serbis.mega.task;

import nz.mega.sdk.*;

public interface BrowserCallback
{
	void onError(MegaError error);
	
	void onLogout();
	
	void onRequestStart();
	
	void onRequestFinish();
	
	void onDownloadFinish(MegaTransfer transfer);
	
	void onMoveFinish();
	
	void progress(int persent);
}
