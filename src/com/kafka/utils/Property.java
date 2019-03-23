package com.kafka.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class Property {
	
	private final Properties configProp = new Properties();

	@SuppressWarnings("unused")
	private Property() {
		try {

			String propertiesFilePath = "/home/ubuntu/eclipse-workspace/KafkaPOC/data/config.properties";
			File file = new File(propertiesFilePath);
			InputStream in = new FileInputStream(file);

			if (in != null) {
				configProp.load(in);
			} else {
				System.out.println("Sorry, unable to find Properties file");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class LazyHolder {
		private static final Property INSTANCE = new Property();
	}

	public static Property getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}
}