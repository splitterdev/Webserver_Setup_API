package org.webserver.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.webserver.db.DataStorage;

public class CSVDatabaseLoader {

	public enum DataHandlingMode {
		BLOCK, ADD, UPDATE, REPLACE
	};

	private String currentTable = null;
	private DataHandlingMode mode = DataHandlingMode.BLOCK;
	private String databaseName;
	private DataStorage storage = null;
	private List<String> currentColumnSetup = null;
	private boolean debugMode = true;

	public CSVDatabaseLoader(String databaseName, DataStorage storage) {
		this.databaseName = databaseName;
		this.storage = storage;
	}

	private static final String TABLE_INDICATOR = "TABLE";
	private static final String COLUMNS_INDICATOR = "COLUMNS";
	private static final String DATA_INDICATOR = "DATA";

	public void loadFile(String filePath) {
		currentTable = null;
		mode = DataHandlingMode.BLOCK;
		currentColumnSetup = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))){
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				String[] list = splitLine(line, ',', '"');
				if (list.length < 2) {
					throw new Exception("Invalid input: \"" + line + "\"");
				}
				String keyword = list[0].trim();
				if (keyword.contentEquals(TABLE_INDICATOR)) {
					currentTable = databaseName + "." + list[1];
					currentColumnSetup = null;
					if (list.length > 2) {
						mode = DataHandlingMode.valueOf(list[2]);
					}
					if (mode == DataHandlingMode.REPLACE) {
						deleteRows();
					}
					debugMode = false;
					if (list.length > 3) {
						debugMode = "DEBUG".contentEquals(list[3]);
						System.out.println("[DBLoadHook] === Debug Mode for table " + currentTable + " ===");
						System.out.println("[DBLoadHook] === Policy " + mode + " ===");
					}
				} else if (keyword.contentEquals(COLUMNS_INDICATOR)) {
					currentColumnSetup = new ArrayList<>();
					for (int i = 1; i < list.length; i++) {
						currentColumnSetup.add(list[i].trim());
					}
					if (currentColumnSetup.isEmpty()) {
						throw new Exception("COLUMNS control received empty config");
					}
				} else if (keyword.contentEquals(DATA_INDICATOR)) {
					if (mode == DataHandlingMode.BLOCK) {
						continue;
					}
					String[] data = new String[list.length - 1];
					for (int i = 0; i < data.length; i++) {
						data[i] = list[i + 1];
					}
					if (mode == DataHandlingMode.UPDATE) {
						deleteRow(data);
					}
					insertRow(data);
				} else {
					throw new Exception("Unrecognized control: \"" + list[0] + "\"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] splitLine(String line, char delimiter, char quoteSign) {
		StringBuilder builder = null;
		List<String> strings = new ArrayList<>();
		boolean insideQuoted = false;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == quoteSign) {
				if (i > 0 && line.charAt(i - 1) != '\\') {
					insideQuoted = !insideQuoted;
				}
			}
			if (c == delimiter && !insideQuoted) {
				if (builder == null) {
					builder = new StringBuilder();
				}
				strings.add(builder.toString());
				builder = null;
			} else {
				if (builder == null) {
					builder = new StringBuilder();
				}
				builder.append(c);
			}
		}
		if (builder != null) {
			strings.add(builder.toString());
		}
		Object[] objects = strings.toArray();
		String[] stringArray = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			stringArray[i] = objects[i].toString();
		}
		return stringArray;
	}

	private boolean executeSQL(String sqlStr) throws SQLException {
		Session session = storage.getSession();
		return session.doReturningWork(new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
        		if (debugMode) {
        			System.out.println("[DBLoadHook] [DEBUG] Next call: \"" + sqlStr + "\"");
        		}
        		Statement statement = connection.createStatement();
        		statement.execute(sqlStr);
        		ResultSet set = statement.getResultSet();
        		if (set != null) {
        			return set.next();
        		}
        		return false;
            }
        });
	}

	private void deleteRows() throws Exception {
		if (currentTable == null) {
			throw new Exception("Delete from unspecified table (missing TABLE control?)");
		}
		executeSQL("DELETE FROM " + currentTable + ";");
	}

	private void deleteRow(String[] data) throws Exception {
		if (currentTable == null) {
			throw new Exception("Delete from unspecified table (missing TABLE control?)");
		}
		if (currentColumnSetup == null) {
			throw new Exception("Unspecified column setup for insert operation (missing COLUMNS control?)");
		}
		int rowIndex = -1;
		for (int i = 0; i < currentColumnSetup.size(); i++) {
			String column = currentColumnSetup.get(i);
			if ("id".equalsIgnoreCase(column)) {
				rowIndex = i;
			}
		}
		if (rowIndex == -1) {
			throw new Exception("Unspecified row id column; column config must contain \"id\" for this operation");
		}
		executeSQL("DELETE FROM " + currentTable + " WHERE (id = " + data[rowIndex] + ");");
	}

	private void insertRow(String[] data) throws Exception {
		if (currentTable == null) {
			throw new Exception("Insert into unspecified table (missing TABLE control?)");
		}
		if (currentColumnSetup == null) {
			throw new Exception("Unspecified column setup for insert operation (missing COLUMNS control?)");
		}
		int rowIndex = -1;
		for (int i = 0; i < currentColumnSetup.size(); i++) {
			String column = currentColumnSetup.get(i);
			if ("id".equalsIgnoreCase(column)) {
				rowIndex = i;
			}
		}
		if (rowIndex == -1) {
			throw new Exception("Unspecified row id column; column config must contain \"id\" for this operation");
		}
		if (executeSQL("SELECT * FROM " + currentTable + " WHERE (id = " + data[rowIndex] + ");")) {
			return;
		}
		StringBuilder builder = new StringBuilder("INSERT INTO ");
		builder.append(currentTable);
		builder.append(" (");
		boolean first = true;
		for (int i = 0; i < data.length; i++) {
			String dataInput = data[i].trim();
			if (dataInput.isEmpty()) {
				continue;
			}
			String column = currentColumnSetup.get(i);
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(column);
		}
		builder.append(") VALUES (");
		first = true;
		for (String dataPart : data) {
			String dataInput = dataPart.trim();
			if (dataInput.isEmpty()) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(dataPart);
		}
		builder.append(");");
		executeSQL(builder.toString());
		System.out.println("[DBLoadHook] Table " + currentTable + " - row modified: id = " + data[rowIndex] + " / policy: " + mode);
	}
}
