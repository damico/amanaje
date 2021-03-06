package org.jdamico.bc.openpgp.tests;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;

import org.jdamico.bc.openpgp.utils.PgpHelper;
import org.jdamico.bc.openpgp.utils.RSAKeyPairGenerator;
import org.junit.Test;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.PGPException;


public class TestBCOpenPGP {

	private boolean isArmored = false;
	private String id = "damico";
	private String passwd = "******";
	private boolean integrityCheck = true;


	private String pubKeyFile = "/tmp/pub.dat";
	private String privKeyFile = "/tmp/secret.dat";

	private String plainTextFile = "/tmp/plain-text.txt";
	private String cipherTextFile = "/tmp/cypher-text.dat";
	private String decPlainTextFile = "/tmp/dec-plain-text.txt";

	@Test
	public void genKeyPair() throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException, PGPException, NoSuchAlgorithmException {

		RSAKeyPairGenerator rkpg = new RSAKeyPairGenerator();

		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator    kpg = KeyPairGenerator.getInstance("RSA", "BC");

		kpg.initialize(1024);

		KeyPair                    kp = kpg.generateKeyPair();

		FileOutputStream    privOut1 = new FileOutputStream(privKeyFile);
		FileOutputStream    pubOut2 = new FileOutputStream(pubKeyFile);

		rkpg.exportKeyPair(privOut1, pubOut2, kp.getPublic(), kp.getPrivate(), id, passwd.toCharArray(), isArmored);


	}

	@Test
	public void encrypt() throws NoSuchProviderException, IOException, PGPException{
//		FileInputStream pubKeyIs = new FileInputStream(pubKeyFile);
//		FileOutputStream cipheredFileIs = new FileOutputStream(cipherTextFile);
//		PgpHelper.getInstance().encryptFile(cipheredFileIs, plainTextFile, PgpHelper.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
//		cipheredFileIs.close();
//		pubKeyIs.close();
	}

	@Test
	public void decrypt() throws Exception{

		FileInputStream cipheredFileIs = new FileInputStream(cipherTextFile);
		FileInputStream privKeyIn = new FileInputStream(privKeyFile);
		FileOutputStream plainTextFileIs = new FileOutputStream(decPlainTextFile);
		PgpHelper.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
		cipheredFileIs.close();
		plainTextFileIs.close();
		privKeyIn.close();
	}

}
