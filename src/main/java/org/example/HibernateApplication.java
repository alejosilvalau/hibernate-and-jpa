// filepath: e:\Job\hibernate-and-jpa\src\main\java\org\example\HibernateApplication.java
package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateApplication {

  public static void main(String[] args) {
    // Create session factory
    SessionFactory factory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();

    // Create session
    Session session = factory.getCurrentSession();

    try {
      // Start a transaction
      session.beginTransaction();

      // Your code here
      System.out.println("Hibernate application running!");

      // Commit transaction
      session.getTransaction().commit();

    } finally {
      factory.close();
    }
  }
}