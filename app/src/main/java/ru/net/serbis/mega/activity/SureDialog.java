package ru.net.serbis.mega.activity;

import android.app.*;
import android.content.*;
import ru.net.serbis.mega.*;

public class SureDialog
{
	public SureDialog(Context context, int title, DialogInterface.OnClickListener onClick)
	{
		new AlertDialog.Builder(context)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(title)
			.setMessage(R.string.mess_are_you_sure)
			.setPositiveButton(R.string.action_ok, onClick)
			.setNegativeButton(R.string.action_cancel, null)
			.show();
	}
}
