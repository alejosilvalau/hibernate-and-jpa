package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

public class HibernateApplication {

  public static void main(String[] args) {
    // Regular application setup
    SessionFactory factory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();

    try {
      System.out.println("Hibernate application running!");

      // Run tests manually
      runTests(factory);

    } finally {
      factory.close();
    }
  }

  private static void runTests(SessionFactory factory) {
    System.out.println("\n=== Running Application Tests ===");

    try {
      // Test 1: Create User
      Long userId = createUserTest(factory);

      // Test 2: Retrieve User
      retrieveUserTest(factory, userId);

      System.out.println("=== All Tests Passed ===\n");
    } catch (AssertionError e) {
      System.err.println("Test failed: " + e.getMessage());
    }
  }

  private static Long createUserTest(SessionFactory factory) {
    Session session = factory.getCurrentSession();
    session.beginTransaction();

    User user = new User("Test User", LocalDate.now());
    session.persist(user);

    session.getTransaction().commit();

    System.out.println("Test 1 - Create User: PASSED (ID: " + user.getId() + ")");
    return user.getId();
  }

  private static void retrieveUserTest(SessionFactory factory, Long userId) {
    Session session = factory.getCurrentSession();
    session.beginTransaction();

    User user = session.find(User.class, userId);
    if (user == null) {
      throw new AssertionError("User not found with ID: " + userId);
    }

    System.out.println("Test 2 - Retrieve User: PASSED (Name: " + user.getName() + ")");
    session.getTransaction().commit();
  }
}