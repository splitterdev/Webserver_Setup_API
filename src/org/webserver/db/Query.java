package org.webserver.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;

public class Query <T> {

	private class OrderBy {
		public OrderBy(String columnName, boolean ascending) {
			this.columnName = columnName;
			this.ascending = ascending;
		}
		public String columnName;
		public boolean ascending;
	}

	private Class<T> resultingClassId;
	private Map<String, String> tableNamesToShortIds = new HashMap<>();
	private StringBuilder whereClause = null;
	private int classCounter = 0;
	private int paramCounter = 0;
	private Map<String, Object> paramValues = new HashMap<>();
	private List<OrderBy> orderByList = new LinkedList<>();
	private Integer queryOffset;
	private Integer queryLimit;

	protected Query(Class<T> classId, String tableName) {
		this.tableNamesToShortIds.put(tableName, "root");
		this.resultingClassId = classId;
	}

	public Query<T> appendClass(Class<?> classId) {
		this.tableNamesToShortIds.put(getTableNameOf(classId), "c_" + classCounter);
		classCounter++;
		return this;
	}

	private static String getTableNameOf(Class<?> classId) {
		Entity entity = classId.getDeclaredAnnotation(Entity.class);
		if (entity != null && entity.name() != null && !entity.name().isEmpty()) {
			return entity.name();
		}
		return classId.getCanonicalName();
	}

	public static <T> Query<T> select(Class<T> classId) {
		return new Query<T>(classId, getTableNameOf(classId));
	}

	private void initializeWhereClause() {
		if (whereClause == null) {
			whereClause = new StringBuilder(" WHERE");
		}
	}

	public Query<T> appendWhere(
			Class<?> objectClass,
			String columnName,
			WhereCompare compareOperator,
			Object constantValue
	) throws QueryException {
		initializeWhereClause();
		String tableName = getTableNameOf(objectClass);
		String tableId = tableNamesToShortIds.get(tableName);
		if (tableId == null) {
			throw new QueryException("Class " + objectClass.getCanonicalName() + " was not registered in query.");
		}
		String paramName = "param" + paramCounter;
		paramCounter++;
		whereClause.append(" ");
		whereClause.append(tableId);
		whereClause.append(".");
		whereClause.append(columnName);
		whereClause.append(" ");
		whereClause.append(compareOperator.getSQLIdentifier());
		whereClause.append(" :");
		whereClause.append(paramName);
		paramValues.put(paramName, constantValue);
		return this;
	}

	public Query<T> appendWhere(
			Class<?> objectClassA,
			String columnNameA,
			WhereCompare compareOperator,
			Class<?> objectClassB,
			String columnNameB
	) throws QueryException {
		initializeWhereClause();
		String tableNameA = getTableNameOf(objectClassA);
		String tableIdA = tableNamesToShortIds.get(tableNameA);
		if (tableIdA != null) {
			throw new QueryException("Class " + objectClassA + " was not registered in query.");
		}
		String tableNameB = getTableNameOf(objectClassB);
		String tableIdB = tableNamesToShortIds.get(tableNameB);
		if (tableIdB != null) {
			throw new QueryException("Class " + objectClassB + " was not registered in query.");
		}
		whereClause.append(" ");
		whereClause.append(tableIdA);
		whereClause.append(".");
		whereClause.append(columnNameA);
		whereClause.append(" ");
		whereClause.append(compareOperator.getSQLIdentifier());
		whereClause.append(" ");
		whereClause.append(tableIdB);
		whereClause.append(".");
		whereClause.append(columnNameB);
		return this;
	}

	public Query<T> appendNotNull(
			Class<?> objectClass,
			String columnName
	) throws QueryException {
		initializeWhereClause();
		String tableName = getTableNameOf(objectClass);
		String tableId = tableNamesToShortIds.get(tableName);
		if (tableId != null) {
			throw new QueryException("Class " + objectClass + " was not registered in query.");
		}
		whereClause.append(" ");
		whereClause.append(tableId);
		whereClause.append(".");
		whereClause.append(columnName);
		whereClause.append(" IS NOT NULL");
		return this;
	}

	public Query<T> appendAnd() {
		initializeWhereClause();
		whereClause.append(" AND");
		return this;
	}

	public Query<T> appendOr() {
		initializeWhereClause();
		whereClause.append(" OR");
		return this;
	}

	public Query<T> appendOpenBracket() {
		initializeWhereClause();
		whereClause.append(" (");
		return this;
	}

	public Query<T> appendCloseBracket() {
		initializeWhereClause();
		whereClause.append(" )");
		return this;
	}

	public Query<T> orderBy(
			Class<?> objectClass,
			String columnName,
			boolean ascending
	) throws QueryException {
		initializeWhereClause();
		String tableName = getTableNameOf(objectClass);
		String tableId = tableNamesToShortIds.get(tableName);
		if (tableId != null) {
			throw new QueryException("Class " + objectClass + " was not registered in query.");
		}
		orderByList.add(new OrderBy(tableName + "." + tableId, ascending));
		return this;
	}

	public Query<T> setQueryOffset(int queryOffset) {
		this.queryOffset = queryOffset;
		return this;
	}

	public Query<T> setQueryLimit(int queryLimit) {
		this.queryLimit = queryLimit;
		return this;
	}

	public Integer getQueryOffset() {
		return queryOffset;
	}

	public Integer getQueryLimit() {
		return queryLimit;
	}

	public String getHibernateSQL() {
		StringBuilder sBuilder = new StringBuilder("SELECT root FROM ");
		StringJoiner joiner = new StringJoiner(", ");
		for (Map.Entry<String, String> e : tableNamesToShortIds.entrySet()) {
			joiner.add(e.getKey() + " " + e.getValue());
		}
		sBuilder.append(joiner.toString());
		if (whereClause != null) {
			sBuilder.append(whereClause);
		}
		if (!orderByList.isEmpty()) {
			sBuilder.append(" ORDER BY");
			for (OrderBy orderBy : orderByList) {
				sBuilder.append(" ");
				sBuilder.append(orderBy.columnName);
				sBuilder.append(orderBy.ascending ? " ASC" : " DESC");
			}
		}
		return sBuilder.toString();
	}

	public void assignParametersToQuery(TypedQuery<?> typedQuery) {
		for (Map.Entry<String, Object> e : paramValues.entrySet()) {
			typedQuery.setParameter(e.getKey(), e.getValue());
		}
	}

	public Class<T> getEntityClass() {
		return resultingClassId;
	}
}
