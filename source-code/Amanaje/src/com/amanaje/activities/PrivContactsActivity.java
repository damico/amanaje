package com.amanaje.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
	public static List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_priv_contacts);
		thisActivity = this;
		
		AsyncTaskManager aTaskMan = new AsyncTaskManager(thisActivity, Constants.LIST_CONTACTS_TYPE, null);
		aTaskMan.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		
		
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
	

}
