package org.webserver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {

	public static class Row {

		private Map<String, String> data = new HashMap<>();

		public Row() {
		}

		public void add(String columnKey, String value) {
			data.put(columnKey, value);
		}

		public String get(String columnKey) {
			return data.get(columnKey);
		}

	}

	private static final String DEFAULT_FIELD_SEPARATOR = ",";
	private static final String DEFAULT_TEXT_SEPARATOR = "\"";

	private String fieldSeparator;
	private String textSeparator;
	private ArrayList<String> columns;
	private BufferedReader reader;

	public CSVReader(String filePath) throws IOException {
		this(filePath, DEFAULT_FIELD_SEPARATOR, DEFAULT_TEXT_SEPARATOR);
	}

	public CSVReader(String filePath, String fieldSeparator, String textSeparator) throws IOException {
		File file = new File(filePath);
		reader = new BufferedReader(new FileReader(file));
		this.fieldSeparator = fieldSeparator;
		this.textSeparator = textSeparator;
		columns = getNextRowArray();
	}

	private ArrayList<String> getNextRowArray() throws IOException {
		ArrayList<String> array = new ArrayList<>();
		boolean parseOver = false;
		String line = reader.readLine();
		if (line != null && !line.isEmpty()) {
			while (!parseOver) {
				int nextOpenTextMatchIndex = line.indexOf(textSeparator);
				int nextCloseTextMatchIndex = line.indexOf(textSeparator, nextOpenTextMatchIndex + 1);
				int nextMatchIndex = line.indexOf(fieldSeparator);
				boolean textSeparatorDetected = false;
				if (nextOpenTextMatchIndex != -1 && nextCloseTextMatchIndex != -1 && nextMatchIndex > nextOpenTextMatchIndex && nextMatchIndex < nextCloseTextMatchIndex) {
					while (nextMatchIndex < nextCloseTextMatchIndex) {
						nextMatchIndex = line.indexOf(fieldSeparator, nextMatchIndex + 1);
						if (nextMatchIndex == -1) {
							break;
						}
					}
					textSeparatorDetected = true;
				}
				if (nextMatchIndex == -1 || nextMatchIndex >= line.length()) {
					parseOver = true;
				} else {
					String item = line.substring(0, nextMatchIndex);
					if (textSeparatorDetected) {
						item = item.trim().replaceAll(textSeparator, "");
					}
					array.add(item);
					line = line.substring(nextMatchIndex + 1);
				}
			}
			if (!line.isEmpty()) {
				if (line.contains(textSeparator)) {
					line = line.trim().replaceAll(textSeparator, "");
				}
				array.add(line);
			}
		}
		return array;
	}

	public Row nextRow() throws IOException {
		if (columns != null && !columns.isEmpty()) {
			ArrayList<String> cells = getNextRowArray();
			if (cells.isEmpty()) {
				return null;
			}
			int minSize = Math.min(cells.size(), columns.size());
			Row row = new Row();
			for (int i = 0; i < minSize; i++) {
				row.add(columns.get(i), cells.get(i));
			}
			return row;
		}
		return null;
	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			reader = null;
		}
	}

	public ArrayList<String> getColumns() {
		return columns;
	}
}
