package com.in28minutes.jpa.hibernate.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "query_get_all_courses", query = "Select c from Course c")
public class Course {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	/**
	 * Default no-argument constructor is explicitly required because we added
	 * another constructor with 'name' argument.
	 */
	protected Course() {
	}

	public Course(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * We only define a 'getter' for property id (no setter).
	 */
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Course [id=%s, name='%s']".formatted(id, name);
	}

	
}
