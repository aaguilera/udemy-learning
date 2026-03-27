package com.in28minutes.jpa.hibernate.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.repository.CourseRepository;

@SpringBootTest
class CourseRepositoryTest {

	@Autowired
	CourseRepository repository;

	@Test
	void findById_basic() {
		var course = repository.findById(10001L);
		assertEquals("JPA in 50 steps", course.getName());
	}

	/**
	 * This test changes the database status, so we use the {@link DirtiesContext}
	 * annotation.
	 */
	@Test
	@DirtiesContext
	void deleteById_basic() {
		repository.deleteById(10002L);
		assertNull(repository.findById(10002L));
	}

	@Test
	@DirtiesContext
	void save_basic() {
		// get course details
		var course = repository.findById(10001L);

		// update details
		course.setName("JPA in 50 steps - Updated");
		repository.save(course);

		// check updated details
		var course1 = repository.findById(10001L);
		assertEquals("JPA in 50 steps - Updated", course1.getName());
	}
}
