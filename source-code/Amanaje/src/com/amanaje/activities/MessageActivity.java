package com.amanaje.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amanaje.R;

public class MessageActivity extends Activity {

	private String body = null;
	private String address = null;
	
	TextView msgTv = null;
	EditText replyEt = null;
	Button replyBt = null;
	Button delBt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    body = extras.getString("body");
		    address = extras.getString("address");
		}
		
		msgTv = (TextView) findViewById(R.id.messageTv);
		msgTv.setText(address+": "+body);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
