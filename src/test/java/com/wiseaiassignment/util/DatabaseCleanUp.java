package com.wiseaiassignment.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.MappingMetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;

@Component
public class DatabaseCleanUp implements InitializingBean {

	@PersistenceContext
	private EntityManager em;

	private final LinkedHashSet<String> tableNames = new LinkedHashSet<>();

	@Override
	public void afterPropertiesSet() {
		SessionFactoryImplementor sessionFactory = em.unwrap(Session.class)
				.getSessionFactory()
				.unwrap(SessionFactoryImplementor.class);

		MappingMetamodelImplementor mapping = sessionFactory.getRuntimeMetamodels().getMappingMetamodel();

		em.getMetamodel().getEntities().forEach(entity -> {
			EntityPersister persister = mapping.findEntityDescriptor(entity.getJavaType());
			if (persister instanceof AbstractEntityPersister p) {
				tableNames.addAll(Arrays.asList(p.getTableNames()));
			}
		});
	}

	@Transactional
	public void truncateAllTables() {
		em.flush();
		em.clear();

		em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		for (String table : tableNames) {
			em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
		}
		em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}
}
