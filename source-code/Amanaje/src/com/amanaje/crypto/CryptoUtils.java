package com.amanaje.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jdamico.bc.openpgp.utils.PgpHelper;
import org.jdamico.bc.openpgp.utils.RSAKeyPairGenerator;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.PGPException;

import android.content.Context;
import android.util.Base64;

import com.amanaje.commons.AppException;
import com.amanaje.commons.AppMessages;
import com.amanaje.commons.Constants;
import com.amanaje.commons.StaticObj;
import com.amanaje.commons.Utils;
import com.amanaje.entities.CryptoAlgoEntity;

public class CryptoUtils {

	private static CryptoUtils INSTANCE = null;

	private CryptoUtils(){}

	public static CryptoUtils getInstance(){
		if(null == INSTANCE) INSTANCE = new CryptoUtils();
		return INSTANCE;
	}

	
	public void genKeyPair(Context context, String id, String privKeyPasswd, boolean isArmored) throws AppException {

		RSAKeyPairGenerator rkpg = new RSAKeyPairGenerator();

		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA", "SC");
		} catch (NoSuchAlgorithmException e) {
			throw new AppException(e);
		} catch (NoSuchProviderException e) {
			throw new AppException(e);
		}

		kpg.initialize(1024);

		KeyPair                    kp = kpg.generateKeyPair();
		
		File privKeyFile = new File(context.getFilesDir(), Constants.PRIV_KEY_FILE_LOCATION);
		File pubKeyFile = new File(context.getFilesDir(), Constants.PUB_KEY_FILE_LOCATION);

		ByteArrayOutputStream out1 = null;
		try {
//			out1 = new FileOutputStream(privKeyFile);
			out1 = new ByteArrayOutputStream();
		} catch (Exception e) {
			throw new AppException(e);
		} finally {
			if(out1!=null) try { out1.flush(); } catch (IOException e) { throw new AppException(e); }
			if(out1!=null) try { out1.close(); } catch (IOException e) { throw new AppException(e); }
		}
		ByteArrayOutputStream out2 = null;
		try {
			//out2 = new FileOutputStream(pubKeyFile);
			out2 = new ByteArrayOutputStream();
		} catch (Exception e) {
			throw new AppException(e);
		} finally {
			if(out2!=null) try { out2.flush(); } catch (IOException e) { throw new AppException(e); }
			if(out2!=null) try { out2.close(); } catch (IOException e) { throw new AppException(e); }
		}

		try {
			rkpg.exportKeyPair(out1, out2, kp.getPublic(), kp.getPrivate(), id, privKeyPasswd.toCharArray(), isArmored);
		} catch (InvalidKeyException e) {
			throw new AppException(e);
		} catch (NoSuchProviderException e) {
			throw new AppException(e);
		} catch (SignatureException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		} catch (PGPException e) {
			throw new AppException(e);
		}
		
		String privHex = null;
		String pubHex = null; 
		
		try {
			pubHex = Utils.getInstance().byteArrayToHexString(out2.toByteArray());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			privHex = Utils.getInstance().byteArrayToHexString(out1.toByteArray());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Utils.getInstance().writeTextToFile(pubKeyFile, pubHex);
		Utils.getInstance().writeTextToFile(privKeyFile, privHex);

	}

	public byte[] pbkdf2(char[] password, byte[] salt, int iterationCount, int keyLength) throws AppException {

		byte[] ret = null;

		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
			try {
				SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
				ret =  secretKey.getEncoded();
			} catch (InvalidKeySpecException ikse) {
				throw new AppException(ikse);
			}
		} catch (NoSuchAlgorithmException nsae) {
			throw new AppException(nsae);
		}

		return ret;
	}

	public byte[] getKeyHash(Context context, char[] passwd) throws AppException{
		byte[] salt = getSalt(context);
		return pbkdf2(passwd, salt, salt.length, Constants.PBKDF2_KEY_LENGTH);
	}

	public byte[] getSalt(Context context){
		String imei = Utils.getInstance().getIMEI(context);
		if(imei != null) return imei.getBytes();
		else return Utils.getInstance().getDeviceData().getBytes();

	}

	private SecretKey genSecretKey(Context context, String password, String algo) throws NoSuchAlgorithmException, InvalidKeySpecException{

		byte[] salt = getSalt(context);
		SecretKeyFactory	factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, salt.length, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), algo); //AES

		return secret;
	}


	public byte[] encSymetric(Context context, String password, byte[] plainContent, String algo) throws AppException{

		byte[] cipherContent = null;
		CryptoAlgoEntity cryptoObj = getCryptoAlgoObjByAlgo(algo);

		try {
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.ENCRYPT_MODE, genSecretKey(context, password, algo), new IvParameterSpec(iv));
			cipherContent = cipher.doFinal(plainContent);
		} catch (NoSuchAlgorithmException e) {
			throw new AppException(e);
		} catch (InvalidKeySpecException e) {
			throw new AppException(e);
		} catch (NoSuchPaddingException e) {
			throw new AppException(e);
		} catch (InvalidKeyException e) {
			throw new AppException(e);
		} catch (IllegalBlockSizeException e) {
			throw new AppException(e);
		} catch (BadPaddingException e) {
			throw new AppException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new AppException(e);
		}

		return cipherContent;


	}




	public byte[] decSymetric(Context context, String password, byte[] cipherContent, String algo) throws AppException {

		byte[] plainContent = null;
		CryptoAlgoEntity cryptoObj = getCryptoAlgoObjByAlgo(algo);

		try {
			Cipher cipher = Cipher.getInstance(cryptoObj.getAlgoInstance()); //"AES/CBC/PKCS5Padding"
			byte[] iv = normalizeIvByteArray(Utils.getInstance().getDeviceData().getBytes(), cryptoObj.getIvLength()); 
			cipher.init(Cipher.DECRYPT_MODE, genSecretKey(context, password, algo), new IvParameterSpec(iv));
			plainContent = cipher.doFinal(cipherContent);

		} catch (NoSuchAlgorithmException e) {
			throw new AppException(e);
		} catch (InvalidKeySpecException e) {
			throw new AppException(e);
		} catch (NoSuchPaddingException e) {
			throw new AppException(e);
		} catch (InvalidKeyException e) {
			throw new AppException(e);
		} catch (IllegalBlockSizeException e) {
			throw new AppException(e);
		} catch (BadPaddingException e) {
			throw new AppException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new AppException(e);
		}

		return plainContent;
	}





	private byte[] normalizeIvByteArray(byte[] notNormalized, int length) throws AppException{
		byte[] iv = new byte[length];

		if(notNormalized != null){

			if(notNormalized.length > length){
				for (int i = 0; i < iv.length; i++) {
					iv[i] = notNormalized[i];
				}
			}else if(notNormalized.length < length){

				for (int i = 0; i < iv.length; i++) {
					if(notNormalized.length != i+1) iv[i] = notNormalized[i];
					else{
						Random generator = new Random(); 
						int r = generator.nextInt(127) + 1;
						if(i % 2 != 0) r = r * -1;
						iv[i] = (byte) r;
					}
				}

			}else iv = notNormalized;
		} else throw new AppException(AppMessages.getInstance().getMessage("CryptoUtils.normalizeIvByteArray.nullSource"));
		return iv;
	}

	private CryptoAlgoEntity getCryptoAlgoObjByAlgo(String algo){
		CryptoAlgoEntity cryptoAlgo = null;
		if(!algo.equalsIgnoreCase("AES")) cryptoAlgo = new CryptoAlgoEntity(algo, "Blowfish/CFB/NoPadding", 8);
		else cryptoAlgo = new CryptoAlgoEntity(algo, "AES/CBC/PKCS5Padding", 16);
		return cryptoAlgo;
	}

	public byte[] genMd5(byte[] source) throws AppException{

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new AppException(e);
		}
		return md.digest(source);
	}

	public void storeKeyInCache(String key, Context context) throws AppException{

		String encHexKey = null;
		try {
			byte[] encKey = encSymetric(context, genKeyInCachePassword(context), key.getBytes("UTF-8"), "AES");
			encHexKey = Utils.getInstance().byteArrayToHexString(encKey);
		} catch (UnsupportedEncodingException e) {
			throw new AppException(e);
		}
		StaticObj.PRIV_KEY_PASSWD = encHexKey;
	}


	public String genKeyInCachePassword(Context context) throws AppException{
		byte[] salt = getSalt(context);
		byte[] saltHash = genMd5(salt);
		String hexStr = null;
		try {
			hexStr = Utils.getInstance().byteArrayToHexString(saltHash);
		} catch (UnsupportedEncodingException e) {
			throw new AppException(e);
		}
		return hexStr;
	}


	public String retrieveKeyFromCache(Context context) throws AppException{

		byte[] decKey = null;
		String decStrKey = null;
		if(StaticObj.PRIV_KEY_PASSWD != null){
			decKey = decSymetric(context,  genKeyInCachePassword(context), Utils.getInstance().hexStringToByteArray(StaticObj.PRIV_KEY_PASSWD), "AES");
			decStrKey = new String(decKey);
		}

		return decStrKey;
	} 

	public byte[] encryptOpenPgp(byte[] pubKeyByteArray, String plainText, File msgFile) throws AppException {
		
		InputStream pubKeyIs = null;
		ByteArrayOutputStream cipheredOutputStream = null;
		try {
			pubKeyIs = new ByteArrayInputStream(pubKeyByteArray);
			cipheredOutputStream = new ByteArrayOutputStream();
			PgpHelper.getInstance().encryptFile(cipheredOutputStream, msgFile, PgpHelper.getInstance().readPublicKey(pubKeyIs), true, true);
		} catch (NoSuchProviderException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		} catch (PGPException e) {
			e.printStackTrace();
			//throw new AppException(e);
		}finally{
			
			if(cipheredOutputStream != null)
				try {
					cipheredOutputStream.flush();
				} catch (IOException e) {
					throw new AppException(e);
				}
				
				try {
					cipheredOutputStream.close();
				} catch (IOException e) {
					throw new AppException(e);
				}
				
			if(pubKeyIs != null)
				try {
					pubKeyIs.close();
				} catch (IOException e) {
					throw new AppException(e);
				}
			
			
		}
		return cipheredOutputStream.toByteArray();
		
	}

	public String decryptOpenPgp(Context ctx, byte[] byteArrayCiphered, String passwd) throws AppException{
		
		String plainText = null;
		InputStream privKeyIn = null;
		File privKeyFile = new File(ctx.getFilesDir(), Constants.PRIV_KEY_FILE_LOCATION);
		String hexPrivKey = Utils.getInstance().getStringFromFile(privKeyFile);
		byte[] byteArrayPrivKey = Utils.getInstance().hexStringToByteArray(hexPrivKey);
		

		InputStream cipheredIs = null;
		ByteArrayOutputStream plainTextOs = null;
//		FileInputStream cipheredFileIs = new FileInputStream(cipherTextFile);
		try {
			cipheredIs = new ByteArrayInputStream(byteArrayCiphered);
			privKeyIn = new ByteArrayInputStream(byteArrayPrivKey);
			plainTextOs = new ByteArrayOutputStream();
			PgpHelper.getInstance().decryptFile(cipheredIs, plainTextOs, privKeyIn, passwd.toCharArray());
			
			byte[] plainTextBa = plainTextOs.toByteArray();
			String plainTextHex = Utils.getInstance().byteArrayToHexString(plainTextBa);
			
			plainText = new String(plainTextOs.toByteArray());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cipheredIs != null)
				try {
					cipheredIs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(plainTextOs != null){
				try {
					plainTextOs.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					plainTextOs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
//		cipheredFileIs.close();
//		plainTextFileIs.close();
//		privKeyIn.close();
		
		return plainText;
	}
	
}
