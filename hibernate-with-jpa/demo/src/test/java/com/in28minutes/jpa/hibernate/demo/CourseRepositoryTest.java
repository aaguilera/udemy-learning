package com.in28minutes.jpa.hibernate.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
