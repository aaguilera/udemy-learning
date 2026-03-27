package com.in28minutes.jpa.hibernate.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.in28minutes.jpa.hibernate.demo.entity.Course;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CourseRepository {

	@Autowired
	EntityManager em;

	public Course findById(Long id) {
		return em.find(Course.class, id);
	}

	public Course save(Course course) {
		if (course.getId() == null) {
			// insert
			em.persist(course);
		} else {
			// update
			em.merge(course);
		}
		return course;
	}

	public void deleteById(Long id) {
		var course = findById(id);
		em.remove(course);
	}
	
	
	public void playWithEntityManager() {
		Course course = new Course("Web Services in 100 steps");
		em.persist(course);
		/*
		 * The following line updates the entity, but the change is not persisted in the DB.
		 * No UPDATE query is run by now.
		 */
		course.setName("Web Services in 100 steps - Updated");
		/*
		 * The following line updates the entity again. After this line is run and When the method returns, the entityManager detects
		 * that this method is inside a transaction, and persists the change to the database (with an UPDATE query)
		 * even if we don't explicitly perform a call to EntityManager.merge()!
		 */
		course.setName("Web Services in 100 steps - Updated one more time");
	}
}
