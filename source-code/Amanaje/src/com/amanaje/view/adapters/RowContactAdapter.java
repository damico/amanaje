package com.amanaje.view.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.amanaje.R;
import com.amanaje.activities.ContactDetailActivity;
import com.amanaje.activities.NewSmsActivity;
import com.amanaje.entities.ConfigEntity;

public class RowContactAdapter extends BaseAdapter implements ListAdapter { 

	private ArrayList<String> list = new ArrayList<String>(); 
	private Context context = null;
	private List<ConfigEntity> cfgContactEntityLst = null;



	public RowContactAdapter(ArrayList<String> list, Context context, List<ConfigEntity> cfgContactEntityLst) { 
		this.list = list; 
		this.context = context; 
		this.cfgContactEntityLst = cfgContactEntityLst;
	} 

	@Override
	public int getCount() { 
		return list.size(); 
	} 

	@Override
	public Object getItem(int pos) { 
		return list.get(pos); 
	} 

	@Override
	public long getItemId(int pos) { 
		return pos;
		//just return 0 if your list items do not have an Id variable.
	} 

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			view = inflater.inflate(R.layout.rowcontact, null);
		} 

		//Handle TextView and display string from your list
		TextView listItemText = (TextView)view.findViewById(R.id.list_item_string); 
		listItemText.setText(list.get(position)); 


		ImageButton settings = (ImageButton)view.findViewById(R.id.settings);


		settings.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) { 
				ConfigEntity element = cfgContactEntityLst.get(position);

				Intent i = new Intent(context, ContactDetailActivity.class);
				i.putExtra("nick", element.getNick());
				i.putExtra("fileName", element.getConfigFileName());
				i.putExtra("number", element.getNumber());
				i.putExtra("pubkey", element.getPublicKey());
				i.putExtra("aKey1", element.getActivationKey1());
				i.putExtra("aKey2", element.getActivationKey2());
				i.putExtra("seed", element.getSeed());
				i.putExtra("thisContactFileName", element.getConfigFileName());
				context.startActivity(i);
			}
		});
		
		
		ImageButton send = (ImageButton)view.findViewById(R.id.send);


		send.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) { 
				ConfigEntity element = cfgContactEntityLst.get(position);
				Intent i = new Intent(context, NewSmsActivity.class);
				i.putExtra("nick", element.getNick());
				i.putExtra("number", element.getNumber());
				i.putExtra("pubkey", element.getPublicKey());
				i.putExtra("seed", element.getSeed());
				context.startActivity(i);
			}
		});

		return view; 
	} 
}