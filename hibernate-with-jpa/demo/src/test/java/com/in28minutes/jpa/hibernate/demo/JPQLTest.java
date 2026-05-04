package com.in28minutes.jpa.hibernate.demo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.in28minutes.jpa.hibernate.demo.entity.Course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@SpringBootTest
class JPQLTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	EntityManager em;

	@Test
	void jpql_basic() {
		Query query = em.createNamedQuery("query_get_all_courses");
		List<?> result = query.getResultList();
		logger.info("JPQL query (raw) [{}] -> {}", query, result);
		assertThat(result).isNotEmpty();
	}

	@Test
	void jpql_typed() {
		String qry = "SELECT c FROM Course c";
		TypedQuery<Course> typedQuery = em.createQuery(qry, Course.class);
		List<Course> result = typedQuery.getResultList();
		logger.info("JPQL query (typed) [{}] -> {}", qry, result);
		assertThat(result).isNotEmpty();
	}
	
	@Test
	void jpql_where() {
		String qry = "SELECT c FROM Course c WHERE name LIKE '%100 steps'";
		TypedQuery<Course> typedQuery = em.createQuery(qry, Course.class);
		List<Course> result = typedQuery.getResultList();
		logger.info("JPQL query (where) [{}] -> {}", qry, result);
		assertThat(result).isNotEmpty();
	}
}
