package org.example;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple App.
 */
public class HibernateFullTest {

  private SessionFactory sessionFactory;


  @BeforeEach
  protected void setUp() throws Exception {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      // The registry would be destroyed by the SessionFactory, but we had trouble
      // building the SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }


  @AfterEach
  protected void tearDown() throws Exception {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }


  @Test
  public void testBasicUsage() {
    Session session = sessionFactory.openSession();

    session.beginTransaction();
    session.remove(new User("Marco's Friend", LocalDate.now()));
    session.getTransaction().commit();
    session.close();

    session = sessionFactory.openSession();
    session.beginTransaction();

    List<User> result = session.createQuery("FROM User", User.class).list();

    for (User user : result) {
      System.out.println("user (" + user.getName() + ") : " + user.getBirthDate());
    }
    session.getTransaction().commit();
    session.close();
  }


  @Test
  public void marco_is_in_the_house() {
    assertThat(1).isGreaterThanOrEqualTo(0);
  }

  @Test
  void save_my_first_object_to_the_db() {
    Object user = new User("Marco", LocalDate.of(1980, 1, 1));

    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.persist(user);
      session.getTransaction().commit();
    }
  }

  @Test
  void hql_fetch_users() {
    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      session.createQuery("SELECT u FROM User u", User.class)
          .list()
          .forEach((u) -> System.out.println(
              "User: " + u.getName() + ", Birthdate: " + u.getBirthDate()));

      System.out.println("Number of users: " +
          session.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult());
      session.getTransaction().commit();

    }
  }
}
