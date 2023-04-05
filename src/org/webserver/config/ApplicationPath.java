package org.webserver.config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ApplicationPath {

	public static String get() throws UnsupportedEncodingException {
		String fullPath = Properties.class.getClassLoader().getResource("").getPath();
		fullPath = URLDecoder.decode(fullPath, "UTF-8");
		fullPath = fullPath.split("/WEB-INF/classes/")[0];
		return fullPath;
	}

}
