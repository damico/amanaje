package com.amanaje.activities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.amanaje.entities.ConfigEntity;
import com.amanaje.entities.SmsEntity;

public class MessageActivity extends Activity {

	private String body = null;
	private String address = null;
	private Activity thisActivity = null;
	private AsyncTaskManager aTaskMan = null;
	private SmsEntity smsEntity = null;

	private long extraEpoch = -0l;
	private String extraPubKey = null;
	private String extraSeed = null;

	TextView msgTv = null;
	EditText replyEt = null;
	Button decryptBt = null;
	Button replyBt = null;
	Button delBt = null;
	EditText privKeyPasswd = null;



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
			extraEpoch = extras.getLong("epoch");

			smsEntity = new SmsEntity();
			smsEntity.setAddress(address);
			smsEntity.setDate(extraEpoch);
			smsEntity.setBody(body);
			smsEntity.setPubKey(extraPubKey);
			smsEntity.setSeed(extraSeed);

			ConfigEntity cfgContactEntity = null;
			try {
				String hexNumber = Utils.getInstance().byteArrayToHexString(address.getBytes());
				String fileName = Constants.PUB_KEY_FILE_LOCATION+"."+hexNumber;
				File contactFile = new File(getFilesDir(), fileName);
				cfgContactEntity = null;
				cfgContactEntity = (Utils.getInstance().configFileToConfigEntity(contactFile));
				cfgContactEntity.setConfigFileName(fileName);
				smsEntity.setPubKey(cfgContactEntity.getPublicKey());
				smsEntity.setSeed(cfgContactEntity.getSeed());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (AppException e) {
				e.printStackTrace();
			}


		}

		msgTv = (TextView) findViewById(R.id.messageTv);
		replyBt = (Button) findViewById(R.id.replyMsgBt);
		replyBt.setEnabled(false);
		replyEt = (EditText) findViewById(R.id.replyEt);
		replyEt.setEnabled(false);
		decryptBt = (Button) findViewById(R.id.decBt);
		delBt = (Button) findViewById(R.id.delMsgBt);
		privKeyPasswd = (EditText) findViewById(R.id.privKeyPasswdEd);


		privKeyPasswd.requestFocus();

		replyBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				smsEntity.setBody(replyEt.getText().toString());
				aTaskMan = new AsyncTaskManager(thisActivity, Constants.SEND_SMS_TYPE, smsEntity);
				aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

			}
		});


		
		
		decryptBt.setOnClickListener(new OnClickListener() {



			String dec = null;

			@Override
			public void onClick(View v) {

				smsEntity.setPrivKeyPasswd(privKeyPasswd.getText().toString());
				privKeyPasswd.setText("");
				privKeyPasswd.setEnabled(false);
				decryptBt.setEnabled(false);


				aTaskMan = new AsyncTaskManager(thisActivity, Constants.DEC_SMS_TYPE, smsEntity);
				aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

				try {
					dec = aTaskMan.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

				msgTv.setText(address+": "+dec);
				if(null != dec){
					replyEt.setEnabled(true);
					replyEt.requestFocus();
					replyBt.setEnabled(true);
				}
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
