package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JPAFullTest {

  private static EntityManagerFactory emf;
  private EntityManager em;

  @BeforeAll
  public static void setUpClass() {
    try {
      // Create EntityManagerFactory
      System.out.println("Creating EntityManagerFactory...");
      emf = Persistence.createEntityManagerFactory("org.example.jpa");
      assertNotNull(emf, "EntityManagerFactory should not be null");
    } catch (Exception e) {
      System.err.println("Failed to create EntityManagerFactory:");
      e.printStackTrace();
      throw e; // Rethrow to fail the test
    }
  }

  @BeforeEach
  public void setUp() {
    // Create new EntityManager before each test
    assertNotNull(emf, "EntityManagerFactory must not be null");
    em = emf.createEntityManager();
    assertNotNull(em, "EntityManager should not be null");
  }

  @AfterEach
  public void tearDown() {
    // Close EntityManager after each test
    if (em != null && em.isOpen()) {
      em.close();
    }
  }

  @AfterAll
  public static void tearDownClass() {
    // Close EntityManagerFactory after all tests
    if (emf != null && emf.isOpen()) {
      emf.close();
    }
  }

  @Test
  public void testBasicUsage() {
    assertNotNull(em, "EntityManager should be initialized before test");

    // Begin transaction
    em.getTransaction().begin();

    // Create a new user
    User user = new User("JPA Test User", LocalDate.now());
    em.persist(user);

    // Commit transaction
    em.getTransaction().commit();

    // Verify user was saved
    assertNotNull(user.getId(), "User ID should be generated");

    // Create a new transaction for querying
    em.getTransaction().begin();

    // Query user
    User foundUser = em.find(User.class, user.getId());
    System.out.println("Found user: " + foundUser);
    // Verify user was found
    assertNotNull(foundUser, "Should find persisted user");
    assertEquals("JPA Test User", foundUser.getName(), "User name should match");

    // Commit transaction
    em.getTransaction().commit();
  }

  @Test
  public void criteriaAPI() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    assertNotNull(cb, "CriteriaBuilder should not be null");

    // Begin transaction
    CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
    Root<User> root = criteriaQuery.from(User.class);
    criteriaQuery.select(root).where(cb.equal(root.get("name"), "JPA Test User"));

    // Execute query
    TypedQuery<User> query = em.createQuery(criteriaQuery);
    List<User> results = query.getResultList();
    results.forEach(System.out::println);
  }
}