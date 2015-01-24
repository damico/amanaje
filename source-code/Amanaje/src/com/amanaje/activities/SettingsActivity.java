package com.amanaje.activities;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amanaje.R;
import com.amanaje.asynctasks.AsyncTaskManager;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.commons.Constants;
import com.amanaje.entities.OpenPgpEntity;

public class SettingsActivity extends Activity {

	EditText privKeyPassword = null;
	EditText myPhoneNumber = null;
	Button genKeyPair = null;
	Button copyPubKey = null;
	AsyncTaskManager aTaskMan = null;
	Activity thisActivity = null;
	private AlertDialog.Builder builder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);
		builder = new AlertDialog.Builder(this);
		privKeyPassword = (EditText) findViewById(R.id.privKeyPasswordEt);
		myPhoneNumber = (EditText) findViewById(R.id.myPhoneNumberEt);
		genKeyPair = (Button) findViewById(R.id.genKeyPairBt);
		copyPubKey = (Button) findViewById(R.id.copyPubKeyBt);
		copyPubKey.setEnabled(false);
		
		thisActivity = this;
		
		
		
		copyPubKey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				aTaskMan = new AsyncTaskManager(thisActivity, Constants.TRANS_COPY_TYPE, new OpenPgpEntity(myPhoneNumber.getText().toString(), null));
				aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
				String result = null;
				try {
					result = (String) aTaskMan.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText("pubKey", result);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(SettingsActivity.this, "Public Key copied to Clipboard!", Toast.LENGTH_LONG).show();
			}
		});
		
		genKeyPair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int err = 0;
				if(!myPhoneNumber.getText().toString().contains("+")){
					err++;

					builder.setMessage("Invalid Phone Number format. Try COUNTRY CODE, AREA CODE and PHONE NUMBER (+1 222 555555).")
					.setTitle("Error")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							return;
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

				}

				if(myPhoneNumber.getText().toString().length()< 5){
					err++;

					builder.setMessage("Too small Phone Number. Try COUNTRY CODE, AREA CODE and PHONE NUMBER.")
					.setTitle("Error")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							return;
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

				}

				if(privKeyPassword.getText().toString().length() == 0){
					err++;

					builder.setMessage("Type the password.")
					.setTitle("Error")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							return;
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

				}

				if(err == 0){

					aTaskMan = new AsyncTaskManager(thisActivity, Constants.GEN_KEY_PAIR_TYPE, new OpenPgpEntity(myPhoneNumber.getText().toString(), privKeyPassword.getText().toString()));
					aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
					copyPubKey.setEnabled(true);

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
