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
		Course course2 = new Course("Angular Js in 100 steps");
		em.persist(course2);
		em.flush();

		em.clear();

		// after em.clear(), the following modifications won't be propagated to the
		// database, even if we use flush():

		course.setName("Web Services in 100 steps - Updated");
		em.flush();

		course2.setName("Angular Js in 100 steps - Updated");
		em.flush();
	}
}
