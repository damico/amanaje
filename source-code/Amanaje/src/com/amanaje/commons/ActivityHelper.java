package com.amanaje.commons;

import com.amanaje.R;
import com.amanaje.activities.ContactDetailActivity;
import com.amanaje.activities.MainActivity;
import com.amanaje.activities.MessageActivity;
import com.amanaje.activities.PrivContactsActivity;
import com.amanaje.activities.SettingsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

public class ActivityHelper {

	private static ActivityHelper INSTANCE = null;

	private ActivityHelper(){}

	public static ActivityHelper getInstance(){
		if(null == INSTANCE) INSTANCE = new ActivityHelper();
		return INSTANCE;
	}
	
	
	public void showAlertDialog(Activity srcActivity, String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(srcActivity);
		builder.setMessage(message)
		.setTitle(title)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				return;
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public boolean onOptionsItemSelected(Activity srcActivity, MenuItem item) {
		boolean ret = false;
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(srcActivity, SettingsActivity.class);
			srcActivity.startActivityForResult(intent, 0);
			ret = true;
			
		} else if (id == R.id.action_add) {
			Intent intent = new Intent(srcActivity, ContactDetailActivity.class);
			srcActivity.startActivityForResult(intent, 0);
			ret = true;
			
		} else if (id == R.id.action_contacts) {
			Intent intent = new Intent(srcActivity, PrivContactsActivity.class);
			srcActivity.startActivityForResult(intent, 0);
			ret = true;
			
		} else if (id == R.id.action_messages) {
			Intent intent = new Intent(srcActivity, MainActivity.class);
			srcActivity.startActivityForResult(intent, 0);
			ret = true;
			
		} 
		return ret;

	}
}
