package com.amanaje.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amanaje.R;
import com.amanaje.commons.ActivityHelper;

public class MessageActivity extends Activity {

	private String body = null;
	private String address = null;
	private Activity thisActivity = null;
	
	TextView msgTv = null;
	EditText replyEt = null;
	Button replyBt = null;
	Button delBt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		thisActivity = this;
		
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return ActivityHelper.getInstance().onOptionsItemSelected(thisActivity, item);
	}
}
