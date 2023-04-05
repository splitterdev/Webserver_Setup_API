package org.webserver.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizedString {

	private static final String XLS_COLUMN_KEY = "keyId";

	private static LocalizedString defaultInstance = null;

	private Map<String, Map<String, String>> localizedMessages = new HashMap<>();
	private String loadedPath = null;

	static {
		try {
			defaultInstance = read("WEB-INF/config/localizedServerData.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LocalizedString() {
	}

	public static LocalizedString read(String path) throws Exception {
		LocalizedString instance = new LocalizedString();
		instance.loadedPath = ApplicationPath.get() + "/" + path;
		CSVReader reader = null;
		try {
			reader = new CSVReader(instance.loadedPath);
			CSVReader.Row row;
			while ((row = reader.nextRow()) != null) {
				String key = row.get(XLS_COLUMN_KEY);
				for (String column : reader.getColumns()) {
					if (column.equals(XLS_COLUMN_KEY)) {
						continue;
					}
					instance.set(column, key, row.get(column));
					System.out.println("Loaded \"" + key + "\" for locale column: [" + column + "]");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return instance;
	}

	public static String get(String key, Locale locale) throws Exception {
		if (defaultInstance == null) {
			throw new Exception("No default instance");
		}
		return defaultInstance.getLocalizedValue(key, locale);
	}

	private String getLocalizedValue(String key, Locale locale) throws Exception {
		if (key == null) {
			throw new Exception("Null key");
		}
		if (locale == null) {
			throw new Exception("Null locale");
		}
		Map<String, String> localizedMap = defaultInstance.localizedMessages.get(locale.getCountry());
		if (localizedMap == null) {
			localizedMap = defaultInstance.localizedMessages.get("EN");
			if (localizedMap == null) {
				throw new Exception("No localized map for locale: " + locale.getCountry());
			}
		}
		String value = localizedMap.get(key);
		if (value == null) {
			throw new Exception("No value for key: " + key + ", locale: " + locale.getCountry());
		}
		return value;
	}

	private void set(String locale, String key, String value) {
		Map<String, String> localizedMap = localizedMessages.get(locale);
		if (localizedMap == null) {
			localizedMap = new HashMap<>();
			localizedMessages.put(locale, localizedMap);
		}
		localizedMap.put(key, value);
	}

	public static void reload() throws Exception {
		if (defaultInstance == null) {
			throw new Exception("No default instance to reload");
		}
		defaultInstance = read(defaultInstance.loadedPath);
	}

}
