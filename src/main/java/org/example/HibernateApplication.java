package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

public class HibernateApplication {

  public static void main(String[] args) {
    // Create session factory
    SessionFactory factory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();

    try {
      // Create a session and start transaction
      Session session = factory.openSession();
      session.beginTransaction();

      System.out.println("Hibernate application running!");

      // Verify schema was created by trying to save a user
      User user = new User("Test User", LocalDate.now());
      session.persist(user);
      System.out.println("User saved with ID: " + user.getId());

      // Query to verify the user was saved
      List<User> users = session.createQuery("FROM User", User.class).list();
      System.out.println("Users in database: " + users.size());
      for (User u : users) {
        System.out.println("User: " + u.getName() + ", Birth date: " + u.getBirthDate());
      }

      // Commit the transaction
      session.getTransaction().commit();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      factory.close();
    }
  }
}