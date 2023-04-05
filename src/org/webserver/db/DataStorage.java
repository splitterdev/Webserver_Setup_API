package org.webserver.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DataStorage {

	private EntityManagerFactory emf;
	private EntityManager entityManager;

	private DataStorage() {
		emf = Persistence.createEntityManagerFactory("webserver_main");
		entityManager = emf.createEntityManager();
	}

	private static DataStorage dataStorage;

	public static DataStorage getDataStorage() {
		if (dataStorage == null) {
			dataStorage = new DataStorage();
		}
		return dataStorage;
	}

	public EntityManager getManager() {
		return entityManager;
	}

	public static EntityManager getEntityManager() {
		return getDataStorage().entityManager;
	}

	public <T extends DataEntity> List<T> execute(Query<T> query) {
		Class<T> classId = query.getEntityClass();
		String sql = query.getHibernateSQL();
		System.out.println(sql);
		TypedQuery<T> typedQuery = DataStorage
				.getEntityManager()
				.createQuery(sql, classId);
		if (query.getQueryOffset() != null) {
			typedQuery.setFirstResult(query.getQueryOffset());
		}
		if (query.getQueryLimit() != null) {
			typedQuery.setMaxResults(query.getQueryLimit());
		}
		query.assignParametersToQuery(typedQuery);
		return typedQuery.getResultList();
	}

	public void create(DataEntity object) {
		Session session = getSession();
		Transaction tr = session.beginTransaction();
		session.persist(object);
		tr.commit();
	}

	public void update(DataEntity object) {
		Session session = getSession();
		Transaction tr = session.beginTransaction();
		session.saveOrUpdate(object);
		tr.commit();
	}

	public void delete(DataEntity object) {
		Session session = getSession();
		Transaction tr = session.beginTransaction();
		session.delete(object);
		tr.commit();
	}

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}
}
