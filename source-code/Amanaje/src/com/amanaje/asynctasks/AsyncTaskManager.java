package com.amanaje.asynctasks;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;

import com.amanaje.commons.AppException;
import com.amanaje.commons.AppMessages;
import com.amanaje.commons.Constants;
import com.amanaje.commons.Utils;
import com.amanaje.crypto.CryptoUtils;
import com.amanaje.entities.OpenPgpEntity;

public class AsyncTaskManager extends AsyncTask<String, Integer, String> {
	
	private ProgressDialog dialog = null;
	private Object obj = null;
	private int type = -1;
	private Activity activity = null;
	
	
	public AsyncTaskManager(Activity activity, int type, Object obj){
		this.type = type;
		this.obj = obj;
		this.activity = activity;
		dialog = new ProgressDialog(activity);
		dialog.setCanceledOnTouchOutside(false);
	}
	
	@Override
    protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage(AppMessages.getInstance().getMessage("GLOBAL.loading"));
        dialog.show();
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		String ret = null;
		
		switch (type) {
		case Constants.GEN_KEY_PAIR_TYPE:
			
			OpenPgpEntity oPgp = (OpenPgpEntity) obj;
			
			try {
				CryptoUtils.getInstance().genKeyPair(activity, oPgp.getPhoneNumber(), oPgp.getPrivKeyPassword(), true);
			} catch (AppException e) {
				e.printStackTrace();
			}
			
			break;
			
		case Constants.TRANS_COPY_TYPE:
			File pubKeyFile = new File(activity.getFilesDir(), Constants.PUB_KEY_FILE_LOCATION);
			
			String pubKeyHexStr = null;
			try {
				
				pubKeyHexStr = Utils.getInstance().getStringFromFile(pubKeyFile);
				
				ret = (pubKeyHexStr);
				

				
			} catch (AppException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		
		return ret;
	}
	
	@Override
	protected void onPostExecute(String result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
	}

}
