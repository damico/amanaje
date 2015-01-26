package com.amanaje.asynctasks;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.amanaje.R;
import com.amanaje.activities.ContactDetailActivity;
import com.amanaje.activities.NewSmsActivity;
import com.amanaje.activities.PrivContactsActivity;
import com.amanaje.commons.AppException;
import com.amanaje.commons.AppMessages;
import com.amanaje.commons.Constants;
import com.amanaje.commons.Utils;
import com.amanaje.crypto.CryptoUtils;
import com.amanaje.entities.ConfigEntity;
import com.amanaje.entities.OpenPgpEntity;
import com.amanaje.entities.SmsEntity;
import com.amanaje.view.adapters.StableArrayAdapter;

public class AsyncTaskManager extends AsyncTask<String, Integer, String> {

	private ProgressDialog dialog = null;
	private Object obj = null;
	private int type = -1;
	private Activity activity = null;
	private List<ConfigEntity> cfgContactEntityLst = null;
	private ListView listview = null;
	private List<String> contacts = null;
	private int currentTask = -1;

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

		case Constants.SAVE_CONTACT_TYPE:
			ConfigEntity configEntity = (ConfigEntity) obj;
			String hexNumber = null;
			try {
				hexNumber = Utils.getInstance().byteArrayToHexString(configEntity.getNumber().getBytes());
				File contact = new File(activity.getFilesDir(), Constants.PUB_KEY_FILE_LOCATION+"."+hexNumber);

				byte[] seed = CryptoUtils.getInstance().pbkdf2(configEntity.getActivationKey1().toCharArray(), configEntity.getActivationKey2().getBytes(), 6, Constants.PBKDF2_KEY_LENGTH);

				configEntity.setSeed(Utils.getInstance().byteArrayToHexString(seed));

				Utils.getInstance().configEntityToConfigFile(configEntity, contact);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (AppException e) {
				e.printStackTrace();
			}

			break;

		case Constants.LIST_CONTACTS_TYPE:
			cfgContactEntityLst = new ArrayList<ConfigEntity>();
			contacts = new ArrayList<String>();
			File activityDir = activity.getFilesDir();
			String[] filesAtDir = activityDir.list();
			for (int i = 0; i < filesAtDir.length; i++) {
				if(filesAtDir[i].contains(Constants.PUB_KEY_FILE_LOCATION+".")){
					File contactFile = new File(activity.getFilesDir(), filesAtDir[i]);
					ConfigEntity cfgContactEntity = null;
					try {
						cfgContactEntity = (Utils.getInstance().configFileToConfigEntity(contactFile));
						cfgContactEntity.setConfigFileName(filesAtDir[i]);
					} catch (AppException e) {
						e.printStackTrace();
					}
					contacts.add(cfgContactEntity.getNick()+": "+cfgContactEntity.getNumber());



					cfgContactEntityLst.add(cfgContactEntity);
				}
			}


			listview = (ListView) activity.findViewById(R.id.listviewcontacts);


			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ConfigEntity element = cfgContactEntityLst.get(position);

					Intent i = new Intent(activity, ContactDetailActivity.class);
					i.putExtra("nick", element.getNick());
					i.putExtra("fileName", element.getConfigFileName());
					i.putExtra("number", element.getNumber());
					i.putExtra("pubkey", element.getPublicKey());
					i.putExtra("aKey1", element.getActivationKey1());
					i.putExtra("aKey2", element.getActivationKey2());
					i.putExtra("seed", element.getSeed());
					i.putExtra("thisContactFileName", element.getConfigFileName());
					activity.startActivity(i);
				}
			});
			currentTask = Constants.LIST_CONTACTS_TYPE;
			break;
			
		case Constants.SEND_SMS_TYPE:
			
			SmsEntity smsEntity = (SmsEntity) obj;
			File msgFile = new File(activity.getFilesDir(), Constants.TEMP_FILE);

			byte[] pubKeyByteArray = null;
			
			try {
				
				System.out.println("PRE ENC **************************** - smsEntity.getBody(): "+smsEntity.getBody());
				//Utils.getInstance().writeTextToFile(msgFile, smsEntity.getBody());
				
				String demo = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog The qui";
				
				smsEntity.setBody(demo);
				Utils.getInstance().writeTextToFile(msgFile, smsEntity.getBody());
				
				//System.out.println(smsEntity.getPubKey());
				
				pubKeyByteArray = Utils.getInstance().hexStringToByteArray(smsEntity.getPubKey());
				
				
				System.out.println("PRE ENC - smsEntity.getBody(): "+smsEntity.getBody());
				
				byte[] encByteArray = CryptoUtils.getInstance().encryptOpenPgp(pubKeyByteArray, smsEntity.getBody(), msgFile);
				SmsManager sms = SmsManager.getDefault();
				
				String base64 = Base64.encodeToString(encByteArray, Base64.DEFAULT);
				
				List<String> parts = Utils.getInstance().getListParts(base64, 140);
				
				Date dt = new Date();
				
				long epoch = dt.getTime()/1000;
				String sEpoch = String.valueOf(epoch);
				int c = 1;
				
				
				for (String part : parts) {
					
					part = sEpoch + ":"+String.valueOf(c)+":"+String.valueOf(parts.size())+":"+part;
					//System.out.println("=====> "+part.length()+" - "+part);
				  //  sms.sendTextMessage(smsEntity.getAddress(), null, part, null, null);
				    c++;
				}
				
				
			} catch (AppException e) {
				e.printStackTrace();
			
			}finally{
				msgFile.delete();
			}
			currentTask = Constants.SEND_SMS_TYPE;
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
		
		switch (currentTask) {
		
		case Constants.SEND_SMS_TYPE:
			Intent i = new Intent(activity, PrivContactsActivity.class);
			activity.startActivity(i);
			break;
			
		case Constants.LIST_CONTACTS_TYPE:
			final StableArrayAdapter adapter = new StableArrayAdapter(activity, android.R.layout.simple_list_item_1, contacts);
			listview.setAdapter(adapter);
			break;

		default:
			break;
		}
		
	}

	

}
