package org.example;

import org.junit.jupiter.api.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPersistenceTest {

  private static SessionFactory sessionFactory;
  private static Long savedUserId;

  @BeforeAll
  public static void setup() {
    sessionFactory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();
  }

  @AfterAll
  public static void tearDown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @Test
  @Order(1)
  public void testCreateUser() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    // Create a new user
    User user = new User("Test User", LocalDate.now());
    session.persist(user);

    session.getTransaction().commit();

    // Save the ID for the next test
    savedUserId = user.getId();

    assertNotNull(savedUserId, "User ID should not be null after persisting");
    session.close();
  }

  @Test
  @Order(2)
  public void testSelectUser() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    // Select the user created in the previous test
    User retrievedUser = session.find(User.class, savedUserId);

    assertNotNull(retrievedUser, "User should be found in the database");
    assertEquals("Test User", retrievedUser.getName(), "User name should match");

    // Alternatively, query all users
    List<User> users = session.createQuery("FROM User", User.class).list();

    assertFalse(users.isEmpty(), "At least one user should exist");
    boolean foundUser = users.stream()
        .anyMatch(u -> u.getId().equals(savedUserId));
    assertTrue(foundUser, "The created user should be in the result list");

    session.getTransaction().commit();
    session.close();
  }

  @Test
  @Order(3)
  public void testCleanup() {
    // Optional cleanup test
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    User user = session.find(User.class, savedUserId);
    if (user != null) {
      session.remove(user);
    }

    session.getTransaction().commit();
    session.close();

    // Verify the user is removed
    session = sessionFactory.openSession();
    User deletedUser = session.find(User.class, savedUserId);
    assertNull(deletedUser, "User should be deleted");
    session.close();
  }
}