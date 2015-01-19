package com.amanaje.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.amanaje.R;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.entities.SmsEntity;

public class MainActivity extends Activity {
	Activity thisActivity = null;
	SimpleCursorAdapter adapter = null;
	SmsEntity[] smsArray = null;

	public Cursor readSmsInbox(){

		// Create Inbox box URI
		Uri inboxURI = Uri.parse("content://sms/inbox");

		// List required columns
		String[] reqCols = new String[] { "_id", "address", "body", "date" };

		// Get Content Resolver object, which will deal with Content Provider
		ContentResolver cr = getContentResolver();

		// Fetch Inbox SMS Message from Built-in Content Provider
		return cr.query(inboxURI, reqCols, null, null, null);



	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		thisActivity = this;
		final ListView listview = (ListView) findViewById(R.id.listview);

		Cursor c = readSmsInbox();
		smsArray = new SmsEntity[c.getColumnCount()];

		System.out.println("============== getColumnCount() =========== "+c.getColumnCount());
		System.out.println("============== getCount() =========== "+c.getCount());
		
		

		if(c.moveToFirst()){
			for (int i = 0; i < c.getCount(); i++) {
				try {
					System.out.println("===================================== "+c.getString(c.getColumnIndexOrThrow("body")).toString());
					smsArray[i] = new SmsEntity(c.getString(c.getColumnIndexOrThrow("body")).toString(), c.getString(c.getColumnIndexOrThrow("address")).toString(), 1l);
					c.moveToNext();
				} catch (Exception e) {
					ActivityHelper.getInstance().showAlertDialog(this, "Exception", e.getMessage());

					e.printStackTrace();

					break;
				}

			}
		}

		
		adapter = new SimpleCursorAdapter(this, R.layout.row, c, new String[] { "body", "address" }, new int[] { R.id.lblMsg, R.id.lblNumber });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent i = new Intent(getApplicationContext(), MessageActivity.class);
				i.putExtra("body", smsArray[position].getBody());
				i.putExtra("address", smsArray[position].getAddress());
				startActivity(i);

			}
		});
		//c.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return ActivityHelper.getInstance().onOptionsItemSelected(thisActivity, item);
	}

	//	private class StableArrayAdapter extends ArrayAdapter<String> {
	//
	//	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	//
	//	    public StableArrayAdapter(Context context, int textViewResourceId,
	//	        List<String> objects) {
	//	      super(context, textViewResourceId, objects);
	//	      for (int i = 0; i < objects.size(); ++i) {
	//	        mIdMap.put(objects.get(i), i);
	//	      }
	//	    }
	//
	//	    @Override
	//	    public long getItemId(int position) {
	//	      String item = getItem(position);
	//	      return mIdMap.get(item);
	//	    }
	//
	//	    @Override
	//	    public boolean hasStableIds() {
	//	      return true;
	//	    }
	//
	//	  }
}
