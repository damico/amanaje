package com.amanaje.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amanaje.R;
import com.amanaje.asynctasks.AsyncTaskManager;
import com.amanaje.commons.ActivityHelper;
import com.amanaje.commons.Constants;
import com.amanaje.entities.OpenPgpEntity;

public class PrivContactsActivity extends Activity {
	
	Activity thisActivity = null;
	ListView listViewContacts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_priv_contacts);
		thisActivity = this;
		
		listViewContacts = (ListView) findViewById(R.id.listviewcontacts);
		
		AsyncTaskManager aTaskMan = new AsyncTaskManager(thisActivity, Constants.LIST_CONTACTS_TYPE, null);
		aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		
		
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
