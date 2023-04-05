package org.webserver.servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.webserver.config.ApplicationPath;
import org.webserver.config.Properties;
import org.webserver.db.DataStorage;
import org.webserver.util.CSVDatabaseLoader;

public class DBLoadHookInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LoaderThread loaderThread = new LoaderThread();
		loaderThread.start();
	}

	public static class LoaderThread extends Thread {

		public LoaderThread() {
		}

		public void run() {
			try {
				CSVDatabaseLoader loader = new CSVDatabaseLoader(
					Properties.getProperty("database.connection.dbName"),
					DataStorage.getDataStorage()
				);
				String rootPath = Properties.getProperty("csvLoad.loaderRootPath").trim();
				if (!rootPath.endsWith("/")) {
					rootPath = rootPath + "/";
				}
				File rootDir = new File(ApplicationPath.get() + "/" + rootPath + "");
				String files[] = rootDir.list();
				if (files != null) {
					for (String file : files) {
						if (file.endsWith(".csv")) {
							String fileIn = rootDir + "/" + file;
							System.out.println("[DBLoadHook] Execute loading from \"" + fileIn + "\"...");
							loader.loadFile(fileIn);
						}
					}
				} else {
					throw new Exception("\"" + rootPath + "\" is not a directory");
				}
				System.out.println("[DBLoadHook] Execution completed.");
			} catch (Exception e) {
				System.out.println("[DBLoadHook] Error occurred: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
