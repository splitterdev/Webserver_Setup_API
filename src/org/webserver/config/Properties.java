package org.webserver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Properties {

	private static Properties defaultInstance = null;

	static {
		try {
			defaultInstance = loadFrom("WEB-INF/config/serverProperties.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String loadedFrom = null;
	private Map<String, String> properties = new HashMap<>();

	public static String getProperty(String key) throws PropertyException {
		if (defaultInstance != null) {
			String propertyValue = defaultInstance.properties.get(key);
			if (propertyValue != null) {
				return propertyValue;
			}
			throw new PropertyException("Not found property " + key);
		}
		throw new PropertyException("Not found default instance of Properties file");
	}

	public static int getIntProperty(String key) throws PropertyException {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NumberFormatException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	public static long getLongProperty(String key) throws PropertyException {
		try {
			return Long.parseLong(getProperty(key));
		} catch (NumberFormatException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	public static double getDoubleProperty(String key) throws PropertyException {
		try {
			return Double.parseDouble(getProperty(key));
		} catch (NumberFormatException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	public static boolean getBooleanProperty(String key) throws PropertyException {
		try {
			return Boolean.parseBoolean(getProperty(key));
		} catch (NumberFormatException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	public static Collection<String> getMultiValueProperty(String key, String separator) throws PropertyException {
		String propertyValue = getProperty(key);
		String[] properties = propertyValue.split(separator);
		Collection<String> collection = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			collection.add(properties[i]);
		}
		return collection;
	}

	public static Collection<Integer> getMultiValueIntProperty(String key, String separator) throws PropertyException {
		String propertyValue = getProperty(key);
		String[] properties = propertyValue.split(separator);
		Collection<Integer> collection = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			collection.add(Integer.parseInt(properties[i].trim()));
		}
		return collection;
	}

	public static Collection<Long> getMultiValueLongProperty(String key, String separator) throws PropertyException {
		String propertyValue = getProperty(key);
		String[] properties = propertyValue.split(separator);
		Collection<Long> collection = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			collection.add(Long.parseLong(properties[i].trim()));
		}
		return collection;
	}

	public static Collection<Double> getMultiValueDoubleProperty(String key, String separator) throws PropertyException {
		String propertyValue = getProperty(key);
		String[] properties = propertyValue.split(separator);
		Collection<Double> collection = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			collection.add(Double.parseDouble(properties[i].trim()));
		}
		return collection;
	}

	public static Collection<Boolean> getMultiValueBooleanProperty(String key, String separator) throws PropertyException {
		String propertyValue = getProperty(key);
		String[] properties = propertyValue.split(separator);
		Collection<Boolean> collection = new ArrayList<>();
		for (int i = 0; i < properties.length; i++) {
			collection.add(Boolean.parseBoolean(properties[i].trim()));
		}
		return collection;
	}

	public static Properties loadFrom(String path) throws Exception {
		Properties properties = new Properties();
		properties.loadedFrom = path;
		File file = new File(ApplicationPath.get() + "/" + path);
		System.out.println("Reading properties from: " + file.getCanonicalPath());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty() && !line.startsWith("#")) {
					String[] kvProp = line.split("=");
					if (kvProp.length == 2) {
						properties.addProperty(kvProp[0].trim(), kvProp[1].trim());
					} else if (kvProp.length > 2) {
						throw new PropertyException("Too many separators for line: \"" + line + "\"");
					} else if (kvProp.length < 2) {
						throw new PropertyException("Invalid line: \"" + line + "\"");
					}
				}
			}
			return properties;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
			}
		}
	}

	private void addProperty(String key, String value) throws PropertyException {
		if (properties.get(key) != null) {
			throw new PropertyException("Property not unique: " + key);
		}
		properties.put(key, value);
	}

	public static void reloadProperties() throws Exception {
		if (defaultInstance != null) {
			defaultInstance = loadFrom(defaultInstance.loadedFrom);
		}
		throw new PropertyException("No default instance to reload");
	}

}
