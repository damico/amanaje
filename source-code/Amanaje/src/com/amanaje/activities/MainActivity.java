package com.amanaje.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

import com.amanaje.R;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.entities.SmsEntity;
import com.amanaje.view.adapters.StableArrayAdapter;

public class MainActivity extends Activity {
	Activity thisActivity = null;
	List<SmsEntity> smsEntityArray = null;
	List<String> smsStrArray = null;
	List<SmsEntity> smsEntityLvArray = null;

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
		smsStrArray = new ArrayList<String>();
		smsEntityArray = new ArrayList<SmsEntity>();

		Cursor c = readSmsInbox();
		smsEntityArray = new ArrayList<SmsEntity>();

		//System.out.println("============== getColumnCount() =========== "+c.getColumnCount());
		//System.out.println("============== getCount() =========== "+c.getCount());


		if(c.moveToFirst()){
			for (int i = 0; i < c.getCount(); i++) {
				try {
					String rawBody = c.getString(c.getColumnIndexOrThrow("body")).toString();
					//System.out.println("===================================== "+rawBody);
					Integer.parseInt(rawBody.substring(0, 10));
					smsEntityArray.add(new SmsEntity(rawBody, c.getString(c.getColumnIndexOrThrow("address")).toString(), 1l));

				}catch (NumberFormatException e) {
					//ActivityHelper.getInstance().showAlertDialog(this, "Exception", e.getMessage());
					//e.printStackTrace();

				}catch (StringIndexOutOfBoundsException e) {
					//ActivityHelper.getInstance().showAlertDialog(this, "Exception", e.getMessage());
					//e.printStackTrace();

				} 
				c.moveToNext();
			}
		}

		if(smsEntityArray.size() > 0){

			smsEntityLvArray = new ArrayList<SmsEntity>();

			Map<String,SmsEntity> smsContent = new HashMap<String, SmsEntity>();
			Map<String,Integer> smsQty = new HashMap<String, Integer>();

			try {

			} catch (Exception e) {
				// TODO: handle exception
			}
			for (int i = 0; i < smsEntityArray.size(); i++) {

				try {
					String header = smsEntityArray.get(i).getBody().substring(0,15);
					String body = smsEntityArray.get(i).getBody().substring(15, smsEntityArray.get(i).getBody().length());
					//System.out.println("*"+header+"*");
					//System.out.println("*"+body+"*");


					SmsEntity sms =  new SmsEntity();
					sms.setBody(body);
					sms.setAddress(smsEntityArray.get(i).getAddress());
					smsContent.put(header, sms);
					String[] bHeader = header.split(":");
					smsQty.put(bHeader[0], Integer.parseInt(bHeader[bHeader.length-1]));                                           
				} catch (Exception e) {
					// TODO: handle exception
				}


			}

			Set<String> set = smsQty.keySet();

			Object[] array = set.toArray();


			Map<Long, SmsEntity> tMap = new HashMap<Long, SmsEntity>();
			List<Long> epochLst =  new ArrayList<Long>();
			
			
			for (int i = 0; i < array.length; i++) {
				try{

					SmsEntity e = null;
					Integer qty = smsQty.get(array[i]);
					StringBuffer sb = new StringBuffer();
					for (int j = 1; j <= qty; j++) {
						String key = array[i]+":"+String.valueOf(j)+":"+String.valueOf(qty)+":";
						e = smsContent.get(key);
						System.out.println("e.getBody().length(): "+ e.getBody().length());
						sb.append(e.getBody());

					}
					String newBody = sb.toString();
					System.out.println(newBody.length());
					Long epoch = Long.parseLong((String)array[i]);
					epoch = epoch * 1000;
					//smsEntityLvArray.add(new SmsEntity(newBody, e.getAddress(), epoch));
					//Date d = new Date(epoch);
					//String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(d);
					//smsStrArray.add(fDate+" "+e.getAddress());
					epochLst.add(epoch);
					tMap.put(epoch, new SmsEntity(newBody, e.getAddress(), epoch));
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			
			
			Collections.sort(epochLst);
			for (Long idxTime : epochLst) {
				smsEntityLvArray.add(tMap.get(idxTime));
				Date d = new Date(idxTime);
				String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(d);
				smsStrArray.add(fDate+" "+tMap.get(idxTime).getAddress());
			}
			
			//c.close();
		}


		final StableArrayAdapter smsLvAdapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, smsStrArray);


		listview.setAdapter(smsLvAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent i = new Intent(getApplicationContext(), MessageActivity.class);
				i.putExtra("body", smsEntityLvArray.get(position).getBody());
				i.putExtra("address", smsEntityLvArray.get(position).getAddress());
				i.putExtra("epoch", smsEntityLvArray.get(position).getDate());
				startActivity(i);

			}
		});
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

	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
		
	}

}
