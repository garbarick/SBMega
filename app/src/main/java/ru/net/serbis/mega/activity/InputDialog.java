package ru.net.serbis.mega.activity;

import android.app.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.mega.*;

public class InputDialog
{
	public interface OnOk
	{
		void run(String text);
	}
	
	public InputDialog(Context context, int title, String defaultValue, final OnOk onOk)
	{
		final EditText editText = new EditText(context);
		editText.setText(defaultValue);
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(R.string.mess_select_value)
			.setView(editText)
			.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					String text = editText.getText().toString();
					onOk.run(text);
				}
			})
			.setNegativeButton(R.string.action_cancel, null)
			.show(); 
	}
}
