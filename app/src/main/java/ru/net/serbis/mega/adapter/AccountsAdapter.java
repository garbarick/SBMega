package ru.net.serbis.mega.adapter;
import android.accounts.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.zip.*;

public class AccountsAdapter extends ArrayAdapter<Account>
{
	private static int layoutId = android.R.layout.simple_list_item_1;
	private static int textId = android.R.id.text1;
	
	public AccountsAdapter(Context context)
	{
		super(context, layoutId, textId);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if (view == null)
		{
			view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
		}
		TextView text = (TextView) view.findViewById(textId);
		text.setText(getItem(position).name);
		
		return view;
	}
}
