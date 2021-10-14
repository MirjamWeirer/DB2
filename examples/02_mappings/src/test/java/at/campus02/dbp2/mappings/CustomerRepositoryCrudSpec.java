package at.campus02.dbp2.mappings;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryCrudSpec {
    //#region test data
    private final String firstname = "Firstname";
    private final String lastname = "Lastname";
    private final AccountType accountType = AccountType.BASIC;
    private final LocalDate registeredSince = LocalDate.now();

    private Customer initDefaultCustomer(){
        Customer customer = new Customer();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setRegisteredSince(registeredSince);
        customer.setAccountType(accountType);
        return customer;
    }
    //#endregion

    //#region setup / tear down
    private EntityManagerFactory factory;
    private EntityManager manager;
    private CustomerRepository repository;
    @BeforeEach
    public void beforeEach(){
        factory = Persistence.createEntityManagerFactory("persistenceUnitName");
        manager = factory.createEntityManager();
        repository = new CustomerRepositoryJpa(factory);
    }
    @AfterEach
    public void afterEach(){
        if (manager.isOpen())
            manager.close();
        if (factory.isOpen())
            factory.close();
    }
    //#endregion

    //#region CRUD: create
    @Test
    public void createNullAsCustomerReturnFalse(){
        //when
        boolean result = repository.create(null);

        //then
       assertFalse(result);

    }
    @Test
    public void createPersistsCustomerInDatabaseAndReturnsTrue(){
        //given
        Customer toCreate = initDefaultCustomer();

        //when
        boolean result = repository.create(toCreate);

        //then
        assertTrue(result);
        //Kontrolle auch in der DB
        EntityManager manager = factory.createEntityManager();
        Customer fromDb = manager.find(Customer.class, toCreate.getId());
        assertEquals(firstname, fromDb.getFirstname());
        assertEquals(lastname, fromDb.getLastname());
        assertEquals(accountType, fromDb.getAccountType());
        assertEquals(registeredSince, fromDb.getRegisteredSince());
    }

    @Test
    public void createExistingCustomerReturnFalse(){
        //given
        Customer toCreate = initDefaultCustomer();

        manager.getTransaction().begin();
        manager.persist(toCreate);
        manager.getTransaction().commit();

        //when
        boolean result = repository.create(toCreate);

        //then
        assertFalse(result);
    }
    //#endregion

    //#region CRUD: read
    @Test
    public void readFindsCustomerInDatabase(){
        //given
        Customer existing = initDefaultCustomer();

        manager.getTransaction().begin();
        manager.persist(existing);
        manager.getTransaction().commit();

        //when
        Customer fromRepository = repository.read(existing.getId());

        //then
        assertEquals(existing.getId(),fromRepository.getId());
        assertEquals(firstname, fromRepository.getFirstname());
        assertEquals(lastname, fromRepository.getLastname());
        assertEquals(accountType, fromRepository.getAccountType());
        assertEquals(registeredSince, fromRepository.getRegisteredSince());
    }

    @Test
    public void readWithNotExistingIdReturnsNull(){
        //when
        Customer fromRepository = repository.read(-1);

        //then
        assertNull(fromRepository);
    }

    @Test
    public void readWithNullAsIdReturnsNull(){
        //when
        Customer fromRepository = repository.read(null);

        //then
        assertNull(fromRepository);
    }
    //#endregion
}
