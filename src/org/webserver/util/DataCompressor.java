package org.webserver.util;

import java.util.HashMap;
import java.util.Map;

public class DataCompressor {

	private DataCompressor() {
	}

	public static Map<Long, Double> mapFromString(String input) {
		Map<Long, Double> output = new HashMap<>();
		String[] kvPairs = input.split("\\|");
		for (String kvPair : kvPairs) {
			//System.out.println(" |Split = " + kvPair);
			String[] vals = kvPair.split(":");
			if (vals.length == 2) {
				//System.out.println("  :Split = " + vals[0] + " / " + vals[1]);
				Long key = Long.parseLong(vals[0]);
				Double value = Double.parseDouble(vals[1]);
				output.put(key, value);
			}
		}
		return output;
	}

	public static String mapToString(Map<Long, Double> input) {
		StringBuilder builder = null;
		for (Map.Entry<Long, Double> e : input.entrySet()) {
			String pair = "" + e.getKey() + ":" + e.getValue();
			if (builder == null) {
				builder = new StringBuilder(pair);
			} else {
				builder.append("|");
				builder.append(pair);
			}
		}
		if (builder == null) {
			return "";
		}
		return builder.toString();
	}
}
