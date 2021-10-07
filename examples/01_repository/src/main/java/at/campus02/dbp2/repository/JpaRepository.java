package at.campus02.dbp2.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaRepository implements CustomerRepository{

    private EntityManager manager;

    public JpaRepository() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("nameOfJpaPersistenceUnit");
        manager = factory.createEntityManager();
    }

    @Override
    public void create(Customer customer) {
        manager.getTransaction().begin();
        manager.persist(customer);
        manager.getTransaction().commit();
    }

    @Override
    public Customer read(String email) {
        //"leert" Persistence Context (Cache) aus, kein managed entities mehr.
        manager.clear();
        return  manager.find(Customer.class,email);

    }

    @Override
    public void update(Customer customer) {
        manager.getTransaction().begin();
        manager.merge(customer);
        manager.getTransaction().commit();
    }

    @Override
    public void delete(Customer customer) {
        manager.getTransaction().begin();
        manager.remove(customer);
        manager.getTransaction().commit();
    }
}
