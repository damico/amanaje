package com.amanaje.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amanaje.R;
import com.amanaje.asynctasks.AsyncTaskManager;
import com.amanaje.commons.Constants;
import com.amanaje.entities.SmsEntity;

public class NewSmsActivity extends Activity {
	
	private Activity thisActivity = null;
	private TextView to = null;
	private EditText sms = null;
	private Button send = null;
	private String extraNick = null;
	private String extraNumber = null;
	private String extraPubKey = null;
	private String extraSeed = null;
	private SmsEntity smsEntity = null;
	private AsyncTaskManager aTaskMan = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_sms);
		
		thisActivity = this;
		
		to = (TextView) findViewById(R.id.targetTv);
		sms = (EditText) findViewById(R.id.smsEt);
		send = (Button) findViewById(R.id.sendMsgBt);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			extraNick = extras.getString("nick");
			extraNumber = extras.getString("number");
			extraPubKey  = extras.getString("pubKey");
			extraSeed  = extras.getString("seed");
			
			smsEntity = new SmsEntity();
			smsEntity.setAddress(extraNumber);
			
			smsEntity.setPubKey(extraPubKey);
			smsEntity.setSeed(extraSeed);
			
		}
		
		to.setText(extraNick+": "+extraNumber);
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				smsEntity.setBody(sms.getText().toString());
				
				aTaskMan = new AsyncTaskManager(thisActivity, Constants.SEND_SMS_TYPE, smsEntity);
				aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, PrivContactsActivity.class);
		this.startActivityForResult(intent, 0);
	}

}
