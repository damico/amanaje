package com.amanaje.asynctasks;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amanaje.R;
import com.amanaje.activities.ContactDetailActivity;
import com.amanaje.activities.MessageActivity;
import com.amanaje.commons.AppException;
import com.amanaje.commons.AppMessages;
import com.amanaje.commons.Constants;
import com.amanaje.commons.Utils;
import com.amanaje.crypto.CryptoUtils;
import com.amanaje.entities.ConfigEntity;
import com.amanaje.entities.OpenPgpEntity;

public class AsyncTaskManager extends AsyncTask<String, Integer, String> {

	private ProgressDialog dialog = null;
	private Object obj = null;
	private int type = -1;
	private Activity activity = null;
	private List<ConfigEntity> cfgContactEntityLst = null;
	private ListView listview = null;
	private List<String> contacts = null;
	private boolean updateContactsLv = false;

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

					System.out.println("=================>  aKey1  "+element.getActivationKey1());
					System.out.println("=================>  aKey2  "+element.getActivationKey2());

					Intent i = new Intent(activity, ContactDetailActivity.class);
					i.putExtra("nick", element.getNick());
					i.putExtra("number", element.getNumber());
					i.putExtra("pubkey", element.getPublicKey());
					i.putExtra("aKey1", element.getActivationKey1());
					i.putExtra("aKey2", element.getActivationKey2());
					i.putExtra("thisContactFileName", element.getConfigFileName());
					activity.startActivity(i);
				}
			});
			updateContactsLv = true;
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
		if(updateContactsLv) {
			final StableArrayAdapter adapter = new StableArrayAdapter(activity, android.R.layout.simple_list_item_1, contacts);
			listview.setAdapter(adapter);
		}
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

}
