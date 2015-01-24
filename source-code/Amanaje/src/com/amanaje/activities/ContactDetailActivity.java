package com.amanaje.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.amanaje.R;
import com.amanaje.asynctasks.AsyncTaskManager;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.commons.Constants;
import com.amanaje.commons.Utils;
import com.amanaje.entities.ConfigEntity;

public class ContactDetailActivity extends Activity {

	private AsyncTaskManager aTaskMan = null;
	private Button save = null;
	private Button send = null;
	private Button del = null;
	private EditText nick = null;
	private EditText number = null;
	private EditText aKey1 = null;
	private EditText aKey2 = null;
	private EditText pubKey = null;
	private Activity thisActivity = null;
	private String extraNick = null;
	private String extraNumber = null;
	private String extraPubKey = null;
	private String extraFileName = null;
	private String extraSeed = null;
	private String extraAkey1 = null;
	private String extraAkey2 = null;
	private String thisContactFileName = null;
	private File thisContact = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);

		save = 		(Button) 	findViewById(R.id.saveContactBt);
		send = 		(Button) 	findViewById(R.id.sendSmsBt);
		del = 		(Button) 	findViewById(R.id.delContactBt);
		nick = 		(EditText) 	findViewById(R.id.nickContactEt);
		number = 	(EditText) 	findViewById(R.id.numberContactEt);
		aKey1 = 	(EditText) 	findViewById(R.id.actKey1ContactEt);
		aKey2 = 	(EditText) 	findViewById(R.id.actKey2ContactEt);
		pubKey = 	(EditText) 	findViewById(R.id.pubKeyContactEt);
		del.setEnabled(false);
		send.setEnabled(false);

		thisActivity = this;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			extraNick = extras.getString("nick");
			extraNumber = extras.getString("number");
			extraPubKey  = extras.getString("pubkey");
			
			extraAkey1 = extras.getString("aKey1"); 
			extraAkey2 = extras.getString("aKey2");
			
			extraSeed = extras.getString("seed");
			extraFileName = extras.getString("fileName");
			
			thisContactFileName = extras.getString("thisContactFileName");
			
			if(extraAkey1 != null && extraAkey2 != null){
				aKey1.setText(extraAkey1);
				aKey2.setText(extraAkey2);
			}

			if(extraPubKey != null) pubKey.setText(extraPubKey);

			if(extraNick != null && extraNumber != null){
				nick.setText(extraNick);
				nick.setEnabled(false);
				number.setText(extraNumber);
				number.setEnabled(false);
				del.setEnabled(true);
				send.setEnabled(true);
			}
		}
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), NewSmsActivity.class);
				i.putExtra("nick", extraNick);
				i.putExtra("number", extraNumber);
				i.putExtra("pubKey", extraPubKey);
				i.putExtra("seed", extraSeed);
				startActivity(i);
			}
		});

		if(!nick.isEnabled()) save.setText("Update");

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int err = 0;
				if(!number.getText().toString().contains("+")){
					err++;

					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Error", "Invalid Phone Number. Try COUNTRY CODE, AREA CODE and PHONE NUMBER.");

				}

				if(number.getText().toString().length()< 5){
					err++;

					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Error","Too small Phone Number. Try COUNTRY CODE, AREA CODE and PHONE NUMBER.");

				}

				if(nick.getText().toString().length() == 0){
					err++;

					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Error","Invalid nick.");

				}

				if(pubKey.getText().toString().length() == 0){
					err++;

					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Error","Invalid public key.");

				}

				if(aKey1.getText().toString().length() == 0 || aKey2.getText().toString().length() == 0 ){
					err++;

					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Error","Invalid Activation Key.");

				}

				if(err == 0){
					
					String message = "User added.";
					
					if(!nick.isEnabled()){
						thisContact = new File(getFilesDir(), thisContactFileName);
						thisContact.delete();
						message = "User updated.";
					}
					ConfigEntity configEntity = new ConfigEntity(nick.getText().toString(), number.getText().toString(), null, pubKey.getText().toString(), aKey1.getText().toString(), aKey2.getText().toString());
					aTaskMan = new AsyncTaskManager(thisActivity, Constants.SAVE_CONTACT_TYPE, configEntity);
					aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
					
					ActivityHelper.getInstance().showAlertDialog(thisActivity, "Success", message);
					
					Intent i = new Intent(getApplicationContext(), PrivContactsActivity.class);
					startActivity(i);
				}
			}
		});

		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!nick.isEnabled()) thisContact.delete();

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
