package com.amanaje.commons;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppMessages {

	private static AppMessages INSTANCE = null;

	private AppMessages(){}

	public static AppMessages getInstance(){
		if(null == INSTANCE) INSTANCE = new AppMessages();
		return INSTANCE;
	}

	public String getMessage(String messageId){

		String locale = Locale.getDefault().toString();

		String msg = null;
		try{

			if(locale.contains("pt")) msg = getPtMap().get(messageId);
			else msg = getEnMap().get(messageId);

		}catch(Exception e){
			msg = messageId + " ["+locale+"]";
		}



		return msg;

	}

	public Map<String, String> getPtMap(){

		Map<String, String> ptMap = new HashMap<String, String>();
		ptMap.put("Utils.createConfig.failedToTransformKeyHash", "Falha ao transformar o hash da chave.");
		ptMap.put("Utils.changeConfig.diffPasswd", "As senhas diferem!");
		ptMap.put("Utils.changeConfig.wrongPasswd", "Senha incorreta!");
		ptMap.put("Utils.changeConfig.nullContext", "O contexto da aplicação é nulo.");
		ptMap.put("Utils.writeTextToFile.failToWriteFile", "Erro ao escrever arquivo.");
		ptMap.put("Utils.getBytesFromFile.fileTooLArge", "O arquivo é muito grande.");
		ptMap.put("GLOBAL.back_button", "Voltar");
		ptMap.put("GLOBAL.version", "Versão: 0.1b");
		ptMap.put("GLOBAL.author", "Autor: Jose Damico <damico@tix11.com>");
		ptMap.put("GLOBAL.source", "Código-fonte: http://github.com/damico");
		ptMap.put("GLOBAL.lic", "Licença: GPL v2");
		ptMap.put("GLOBAL.authButton", "Desbloquear");
		ptMap.put("GLOBAL.key_textView", "Chave:");
		ptMap.put("YapeaAuthActivity.onCreate.WrongKey", "Senha incorreta!");
		ptMap.put("GLOBAL.about", "Sobre");
		ptMap.put("GLOBAL.algoTv", "Algoritmo:");
		ptMap.put("GLOBAL.oldPtV", "Senha antiga:");
		ptMap.put("GLOBAL.newPtV", "Nova senha:");
		ptMap.put("GLOBAL.newP2tV", "Repita a nova senha:");
		ptMap.put("GLOBAL.reset_button", "Reset");
		ptMap.put("GLOBAL.save_config_button", "Salvar");
		ptMap.put("YapeaConfigActivity.onCreate.dumpAppData", "Apagar todos os dados da aplicação?");
		ptMap.put("GLOBAL.yes", "Sim");
		ptMap.put("GLOBAL.no", "Não");
		ptMap.put("GLOBAL.cam_button", "Camera");
		ptMap.put("GLOBAL.gallery_button", "Galeria");
		ptMap.put("GLOBAL.config_button", "Config");
		ptMap.put("YapeaMainActivity.onCreate.keyInCache", "Chave armazenada em memória.");
		ptMap.put("CryptoUtils.normalizeIvByteArray.nullSource", "A fonte é nula.");
		ptMap.put("YapeaAuthActivity.onCreate.failToStoreKeyInCache", "Erro ao armazenar a chave em cache.");
		ptMap.put("GLOBAL.clear_cache_button", "Limpar cache");
		ptMap.put("GLOBAL.cache_cleaned", "Cache limpo.");
		ptMap.put("GLOBAL.panicTv", "Senha de pânico:");

		return ptMap;
	}

	public Map<String, String> getEnMap(){

		Map<String, String> enMap = new HashMap<String, String>();
		enMap.put("Utils.createConfig.failedToTransformKeyHash", "Failed to transform key hash.");
		enMap.put("Utils.changeConfig.diffPasswd", "The password are different!");
		enMap.put("Utils.changeConfig.wrongPasswd", "Wrong password!");
		enMap.put("Utils.changeConfig.nullContext", "The application context is null.");
		enMap.put("Utils.writeTextToFile.failToWriteFile", "Fail to write file.");
		enMap.put("Utils.getBytesFromFile.fileTooLArge", "File is too large.");
		enMap.put("GLOBAL.back_button", "Back");
		enMap.put("GLOBAL.version", "Version: 0.1b");
		enMap.put("GLOBAL.author", "Author: Jose Damico <damico@tix11.com>");
		enMap.put("GLOBAL.source", "Source code: http://github.com/damico");
		enMap.put("GLOBAL.lic", "Licence: GPL v2");
		enMap.put("GLOBAL.authButton", "Unlock");
		enMap.put("GLOBAL.key_textView", "Key:");
		enMap.put("YapeaAuthActivity.onCreate.WrongKey", "Wrong password!");
		enMap.put("GLOBAL.about", "About");
		enMap.put("GLOBAL.algoTv", "Algorithm:");
		enMap.put("GLOBAL.oldPtV", "Old password:");
		enMap.put("GLOBAL.newPtV", "New password:");
		enMap.put("GLOBAL.newP2tV", "Type new password again:");
		enMap.put("GLOBAL.reset_button", "Reset");
		enMap.put("GLOBAL.save_config_button", "Save");
		enMap.put("YapeaConfigActivity.onCreate.dumpAppData", "Clear all application data?");
		enMap.put("GLOBAL.yes", "Yes");
		enMap.put("GLOBAL.no", "No");
		enMap.put("GLOBAL.cam_button", "Camera");
		enMap.put("GLOBAL.gallery_button", "Gallery");
		enMap.put("GLOBAL.config_button", "Config");
		enMap.put("YapeaMainActivity.onCreate.keyInCache", "Key stored in memory.");
		enMap.put("CryptoUtils.normalizeIvByteArray.nullSource", "The source is null.");
		enMap.put("YapeaAuthActivity.onCreate.failToStoreKeyInCache", "Failed trying to store key in cache.");
		enMap.put("GLOBAL.clear_cache_button", "Clear cache");
		enMap.put("GLOBAL.cache_cleaned", "Cache cleaned.");
		enMap.put("GLOBAL.panicTv", "Panic password:");

		/*
		 enMap.put("Utils.createConfig.failedToTransformKeyHash", "");
		 enMap.put("Utils.changeConfig.diffPasswd", "");
		 enMap.put("Utils.changeConfig.wrongPasswd", "");
		 enMap.put("Utils.changeConfig.nullContext", "");
		 enMap.put("Utils.writeTextToFile.failToWriteFile", "");
		 enMap.put("Utils.getBytesFromFile.fileTooLArge", "");
		 enMap.put("GLOBAL.back_button", "");
		 enMap.put("GLOBAL.version", "");
		 enMap.put("GLOBAL.author", "");
		 enMap.put("GLOBAL.source", "");
		 enMap.put("GLOBAL.lic", "");
		 enMap.put("GLOBAL.authButton", "");
		 enMap.put("GLOBAL.key_textView", "");
		 enMap.put("YapeaAuthActivity.onCreate.WrongKey", "");
		 enMap.put("GLOBAL.about", "");
		 enMap.put("GLOBAL.algoTv", "");
		 enMap.put("GLOBAL.oldPtV", "");
		 enMap.put("GLOBAL.newPtV", "");
		 enMap.put("GLOBAL.newP2tV", "");
		 enMap.put("GLOBAL.reset_button", "");
		 enMap.put("GLOBAL.save_config_button", "");
		 enMap.put("YapeaConfigActivity.onCreate.dumpAppData", "");
		 enMap.put("GLOBAL.yes", "");
		 enMap.put("GLOBAL.no", "");
		 enMap.put("GLOBAL.cam_button", "");
		 enMap.put("GLOBAL.gallery_button", "");
		 enMap.put("GLOBAL.config_button", "");
		 enMap.put("YapeaMainActivity.onCreate.keyInCache", "");
		 enMap.put("CryptoUtils.normalizeIvByteArray.nullSource", "");
		 */


		return enMap;
	}
}
