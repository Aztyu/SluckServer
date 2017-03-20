package com.sluck.server.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	public static Properties load(String file){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = PropertiesLoader.class.getClassLoader().getResourceAsStream(file);
			prop.load(input);
		} catch (IOException ex) {
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		
		return prop;
	}
}
