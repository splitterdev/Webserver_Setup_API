package org.webserver.info;

import java.util.Locale;

import org.webserver.config.LocalizedString;

public class ServerInfo {

	public static ServerInfo getServerInfo(Locale locale) {
		return new ServerInfo(locale);
	}

	private Locale locale;

	private ServerInfo(Locale locale) {
		this.locale = locale;
	}

	private static final String APP_NAME_KEY = "server.appName";
	
	private String getString(String key, String notFound) {
		try {
			return LocalizedString.get(key, locale);
		} catch (Exception e) {
			e.printStackTrace();
			return notFound;
		}
	}

	public String getAppName() {
		return getString(APP_NAME_KEY, "Page");
	}

	private final String allowedCharacters = "ABCDEFGHIJKLMNOQPRSTUWVXYZ";
	public String getRandomConfirmationString() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sBuilder.append(allowedCharacters.charAt((int)(Math.random() * allowedCharacters.length())));
		}
		return sBuilder.toString();
	}

}
