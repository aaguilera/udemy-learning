package com.in28minutes.jpa.hibernate.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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
	 * annotation to make Spring test runner to reset the database status after this
	 * test is run, thus avoiding other tests which depend on the same data to fail.
	 */
	@Test
	@DirtiesContext
	void deleteById_basic() {
		repository.deleteById(10002L);
		assertNull(repository.findById(10002L));
	}
}
