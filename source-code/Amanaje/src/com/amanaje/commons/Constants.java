package com.amanaje.commons;

public interface Constants {
	
	public static final String CONFIG_FILE = ".amanaje.config";
	public static final String XML_CONFIG_ROOT_TAG = "amanaje";
	public static final String XML_CONFIG_CONTACT_TAG = "contact";
	public static final String XML_CONFIG_CONTACT_NICK_ATTRIB = "nick";
	public static final String XML_CONFIG_CONTACT_NUMBER_ATTRIB = "number";
	public static final String XML_CONFIG_CONTACT_SEED_ATTRIB = "seed";
	public static final String XML_CONFIG_KEY_TAG = "key";
	public static final int PBKDF2_KEY_LENGTH = 64;
	public static final int TAKE_PHOTO_CODE = 0;
	public static final String TIMESTAMP_FORMAT = "yyyyMMMdd_HH_mm_ss";
	public static final String XML_CONFIG_KEY_PANICPASSWD_ATTRIB = "panicPasswd";
	public static final String XML_CONFIG_KEY_PANICNUMBER_ATTRIB = "panicNumber";
	public static final String PRIV_KEY_FILE_LOCATION = ".amanaje.priv.key";
	public static final String PUB_KEY_FILE_LOCATION = ".amanaje.pub.key";
	
	public static final int GEN_KEY_PAIR_TYPE = 1000;
	public static final int TRANS_COPY_TYPE = 1001;
	public static final int SAVE_CONTACT_TYPE = 1002;
	public static final int LIST_CONTACTS_TYPE = 1003;
	

}
