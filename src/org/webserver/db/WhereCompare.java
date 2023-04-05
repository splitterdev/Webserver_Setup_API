package org.webserver.db;

public enum WhereCompare {
	EQUAL("="), GREATER(">"), LESS("<"), GREATER_OR_EQUAL(">="), LESS_OR_EQUAL("<="), NOT_EQUAL("!="), LIKE("LIKE");
	private String sqlIdentifier;
	private WhereCompare(String sqlIdentifier) {
		this.sqlIdentifier = sqlIdentifier;
	}
	public String getSQLIdentifier() {
		return sqlIdentifier;
	}
}
