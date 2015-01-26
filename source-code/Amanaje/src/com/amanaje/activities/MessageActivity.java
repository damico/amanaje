package com.amanaje.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amanaje.R;
import com.amanaje.asynctasks.AsyncTaskManager;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.commons.AppException;
import com.amanaje.commons.Constants;
import com.amanaje.commons.Utils;
import com.amanaje.crypto.CryptoUtils;
import com.amanaje.entities.SmsEntity;

public class MessageActivity extends Activity {

	private String body = null;
	private String address = null;
	private Activity thisActivity = null;
	private AsyncTaskManager aTaskMan = null;
	private SmsEntity smsEntity = null;
	
	private String extraNumber = null;
	private String extraPubKey = null;
	private String extraSeed = null;
	
	TextView msgTv = null;
	EditText replyEt = null;
	Button decryptBt = null;
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

			extraPubKey  = extras.getString("pubKey");
			extraSeed  = extras.getString("seed");
			
			smsEntity = new SmsEntity();
			smsEntity.setAddress(address);
			
			smsEntity.setPubKey(extraPubKey);
			smsEntity.setSeed(extraSeed);
		}
		
		msgTv = (TextView) findViewById(R.id.messageTv);
		replyEt = (EditText) findViewById(R.id.replyEt);
		decryptBt = (Button) findViewById(R.id.decBt);
		
		String dec = null;
		
		try {
			byte[] byteArrayCiphered = Base64.decode(body, Base64.DEFAULT);
			
			System.out.println("smsEntity.getSeed(): "+smsEntity.getSeed());
			
			byte[] decSymetric = CryptoUtils.getInstance().decSymetric(getApplicationContext(), smsEntity.getSeed(), byteArrayCiphered, "BLOWFISH");
			dec = CryptoUtils.getInstance().decryptOpenPgp(getApplicationContext(), decSymetric, "qwer");

		} catch (AppException e) {
			e.printStackTrace();
		}
		
		msgTv.setText(address+": "+dec);
		
		replyBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				smsEntity.setBody(replyEt.getText().toString());
				
				aTaskMan = new AsyncTaskManager(thisActivity, Constants.SEND_SMS_TYPE, smsEntity);
				aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
			}
		});
		
		
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
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivityForResult(intent, 0);
	}
}
