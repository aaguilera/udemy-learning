# Mastering Hibernate and JPA with Hibernate in 100 steps

[Course Materials - GitHub repo](https://github.com/in28minutes/jpa-with-hibernate)

## Section 5: JPA and Hibernate in Depth

The `EntityManager` is an interface chich implements a _persistence context_.
The persistence context keeps track of the entities and their changes.

### Playing with EntityManager

The `merge` method in JPA's `EntityManager` is used to update an existing persistent entity or to insert a new entity into the database. Here are some key points regarding its functionality:

- Reattaches Detached Entities: When you call `merge`, it takes a detached entity and reattaches it to the persistence context. This is crucial when you have an entity that has been modified outside of a transaction.

- Update or Insert Logic: The `merge` method checks if the entity has an ID (identifier). If the ID is set, it updates the existing entity in the database. If the ID is not set, it will insert a new entity. Essentially, it abstracts away the logic of deciding whether to insert or update.

- Return Value: After invoking `merge`, it returns a managed instance of the entity that is now associated with the persistence context. This returned instance may not be the same as the original entity instance that was passed to `merge`.

- Flushing Changes: Changes made to entities in the persistence context are not immediately written to the database until the transaction is committed. You can manually flush changes using the `flush` method if needed.

- Difference from persist: While `merge` can be used for both updates and inserts, the `persist` method should only be used for new entities. If you try to use `persist` on an entity with an assigned ID, an exception will be thrown.

When inside a transaction, the `EntityManager` will persist entity changes even if we don't call `EntityManager.merge()`.

For example, the following method:

```java
@Transactional
public void playWithEntityManager() {
  Course course = new Course("Web Services in 100 steps");
  em.persist(course);

  /* The following line updates the entity, but the change 
     is not persisted in the DB. No UPDATE query is run by now. */
  course.setName("Web Services in 100 steps - Updated");

  /* The following line updates the entity again. 
     After this line is run and the method returns, 
     the EntityManager detects that the entity is has pending modifications
     and persists the changes to the database (with an UPDATE query),
     even though are not performing a call to em.merge(course)
     before the method ends. */
  course.setName("Web Services in 100 steps - Updated one more time");
}
```

this will generate only two SQL queries:

```sql
-- em.persist(course)
insert into course (name, id) values(?, ?)
-- last call to course.setName(...)
update course set name=? where id=?
```

note that the first modification is not propagated to the DB!

### EntityManager methods `flush()`, `clear()` and `detach()`

- `flush()`: Synchronize changes held in the persistence context to the underlying database.
- `detach(entity)`: Evict the given managed or removed entity from the persistence context, causing the entity to become immediately detached. Unflushed changes made to the entity, if any, including deletion of the entity, will never be synchronized to the database.
- `clear()`: Clear the persistence context, causing all managed entities to become detached. Changes made to entities that have not already been flushed to the database will never be made persistent.

```java
@Transactional
void method(EntityManager em) {
  em.persist(entity1);
  em.persist(entity2);

  /* force persist changes to the database (even the transaction did not finish) */
  em.flush(); 
  em.clear(); 
  
  /* after clear(), the EntityManager clears the persistence context 
     («forgets») managed entitites, and the following changes will not
     be propagated to the database. */

  entity1.setName("New name");
  em.flush();
}
```

After calling `detach(entity)`, the method `merge(entity)` reattaches the detached entity, applies changes made to it, and ensures those changes are saved back to the database.

### EntityManager methods: `refresh()`

The method `EntityManager.refresh()`: Refresh the state of the given managed entity instance from the database, overwriting unflushed changes made to the entity, if any. This operation cascades to every entity related by an association marked `cascade=REFRESH`.


```java
@Transactional
void method(EntityManager em) {
  Course course = new Course("Jpa in 100 steps");
  em.persist(course);
  em.flush(); // propagate changes to the DB
  
  /* perform changes to the entity */
  course.setName("Jpa in 100 steps - Updated");

  /* the following method call retrieves «course» from the database,
     so the previous change to the entity is lost */
  em.refresh(course);

  entity1.setName("New name");
  em.flush();
}
em.refresh(entity);
```

### JPQL basics

JPQL (Java Persistence Query Language).

In SQL, we query data from _tables_. In JPQL, we query data from _entities_.
JPQL queries are automatically converted to SQL by the JPA implementation (Hibernate).

```sql
-- SQL query: Here 'COURSE' refers to the COURSE table:
SELECT * FROM COURSE

-- JPQL query: here 'Course' refers to the Course entity:
SELECT c FROM Course c
```

```java
/* raw query */
Query query = em.createQuery("SELECT c FROM Course c");
List<?> result = query.getResultList();

/* typed query*/
TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
List<Course> result = query.getResultList();

/* typed query (_WHERE_ clause) */
TypedQuery<Course> query = em.createQuery(
   "SELECT c FROM Course c WHERE name LIKE '%100 steps'", 
   Course.class);
List<Course> result = query.getResultList();
```

### JPA And Hibernate Annotations - `@Table`

`@Table`: used to define the name of the table.

For exemple:

```java
@Entity
@Table(name="CourseDetails")
public class Course {...}
```

The previous annotation will map the `Course` entity to the `course_details` database table. By default, the naming convention will translate the table name from _CamelCase_ to _snake\_case_.

The `@Table` annotation is only required if the entity name is different from the table name.

### JPA and Hibernate Annotations - `@Column`

`@Column`: used to define the column to which an  entity's attribute is mapped to.

For example:

```java
@Entity
public class Course {
   @Column(name="fullname")
   private String name;
}
```

In the previous example, the `name` attribute will be mapped to the column `course.fullname` (`course` is the name of the table to which the entity is being mapped).

Attributes of the `@Column` annotation:

- `name`: the column name.
- `nullable` (true): wether the value of the column can be NULL. Default true
- `unique` (false): default false
- `insertable` (true): wether this column is included in SQL INSERT statements
- `updatable` (true): wether this column is included in SQL UPDATE statements
- `length` (255): maximum length (for string-valued columns)
- `precision` (0): precision for a decimal column
- `scale` (0): scale for a decimal column

### JPA and Hibernate Annotations - `@UpdateTimestamp` & `@CreationTimestamp`

**WARNING**: These are Hibernate annotations (not JPA annotations)

Sometimes we need to store the date at which some row was created, and/or the last time it was updated.

```java
@UpdateTimestamp
private LocalDateTime lastUpdateDate;

@CreationTimestamp
private LocalDateTime createdDate;
```

These two annotations tell Hibernate to automatically initialize or update those columns when the entity is created or updated. There is no need for us to explicitly supply values for these columns.

### JPA and Hibernate Annotations - `@NamedQuery` and `@NamedQueries`

`@NamedQuery`: associate a query with a name, so we can use the query in different places by using its name.

```java
@Entity
@NamedQuery(name="query_get_all_courses", query = "select c from Course c")
public class Course {
   ...
}

// in another class:
{
   Query query1 = em.createNamedQuery("query_get_all_courses")
   TypedQuery<Course> query2 = em.createNamedQuery("query_get_all_courses", Course.class)
}
```

When we want to define multiple named queries we can not use several `@NamedQuery` annotations (because
it is not repeatable). So we use `@NamedQueries` instead. For example:

```java
@Entity
@NamedQueries(value={
      @NamedQuery(
         name="query_get_all_courses", 
         query="select c from Course c"),
      @NamedQuery(
         name="query_get_100_step_courses", 
         query="select c from Course c where name like '%100 steps'")
   })
public class Course {...}
```

### Native Queries - Basics

