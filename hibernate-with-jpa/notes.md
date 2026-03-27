# Mastering Hibernate and JPA with Hibernate in 100 steps

[Course Materials - GitHub repo](https://github.com/in28minutes/jpa-with-hibernate)



# Section 5

## Playing with EntityManager

When inside a transaction, the `EntityManager` will persist entity changes even if we don't call
`EntityManager.merge()`.

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

## EntityManager methods `flush()`, `clear()` and `detach()`

- `flush()`: Synchronize changes held in the persistence context to the underlying database.
- `detach(entity)`: Evict the given managed or removed entity from the persistence context, causing the entity to become immediately detached. Unflushed changes made to the entity, if any, including deletion of the entity, will never be synchronized to the database.
- `clear()`: Clear the persistence context, causing all managed entities to become detached. Changes made to entities that have not already been flushed to the database will never be made persistent.