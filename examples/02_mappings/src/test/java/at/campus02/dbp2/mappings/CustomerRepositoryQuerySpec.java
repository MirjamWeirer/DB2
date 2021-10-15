package at.campus02.dbp2.mappings;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRepositoryQuerySpec {
    private EntityManagerFactory factory;
    private EntityManager manager;
    private CustomerRepository repository;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;
    private Customer customer5;
    private Customer customer6;
    private Customer customer7;

    private void setupCommonTestdata(){
        customer1 = new Customer();
        customer1.setFirstname("Anna");
        customer1.setLastname("Anders");
        customer1.setAccountType(AccountType.BASIC);
        customer1.setRegisteredSince(LocalDate.of(2021,1,1));

        customer2 = new Customer();
        customer2.setFirstname("Bernd");
        customer2.setLastname("Braun");
        customer2.setAccountType(AccountType.PREMIUM);
        customer2.setRegisteredSince(LocalDate.of(2021,2,2));

        customer3 = new Customer();
        customer3.setFirstname("Charlie");
        customer3.setLastname("Chaplin");
        customer3.setAccountType(AccountType.PREMIUM);
        customer3.setRegisteredSince(LocalDate.of(2021,3,3));

        customer4 = new Customer();
        customer4.setFirstname("Natali");
        customer4.setLastname("Lujic");
        customer4.setAccountType(AccountType.PREMIUM);
        customer4.setRegisteredSince(LocalDate.of(2021,4,4));

        customer5 = new Customer();
        customer5.setFirstname("Mirjam");
        customer5.setLastname("Weirer");
        customer5.setAccountType(AccountType.PREMIUM);
        customer5.setRegisteredSince(LocalDate.of(2021,5,5));

        customer6 = new Customer();
        customer6.setFirstname("Stefan");
        customer6.setLastname("Maier");
        customer6.setAccountType(AccountType.BASIC);
        customer6.setRegisteredSince(LocalDate.of(2021,6,6));

        customer7 = new Customer();
        customer7.setFirstname("Lisa");
        customer7.setLastname("Maierhofer");
        customer7.setAccountType(AccountType.BASIC);
        customer7.setRegisteredSince(LocalDate.of(2021,7,7));

        manager.getTransaction().begin();
        manager.persist(customer5);
        manager.persist(customer4);
        manager.persist(customer1);
        manager.persist(customer7);
        manager.persist(customer2);
        manager.persist(customer6);
        manager.persist(customer3);
        manager.getTransaction().commit();
    }

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

    @Test
    public void getAllCustomersReturnsAllCustomerFromDbSortedByRegistrationsDate(){
        //given
        setupCommonTestdata();

        //when
        List<Customer> sortedCustomers = repository.getAllCustomers();

        //then

        assertThat(sortedCustomers, contains(customer1,customer2,customer3,customer4,customer5,customer6,customer7));
//        assertIterableEquals(
//                Arrays.asList(customer1, customer2, customer3, customer4, customer5, customer6, customer7),
//                sortedCustomers
//        );
    }

    @Test
    public void getAllCustomersonEmptyDatabaseReturnsEmptyList(){
        //when
        List<Customer> sortedCustomers = repository.getAllCustomers();

        //then
        assertThat(sortedCustomers, is(empty()));
        //assertTrue(sortedCustomers.isEmpty());
    }

    @Test
    public void findByAccountTypeReturnsMatchingCustomers(){
        //given
        setupCommonTestdata();

        //when
        List<Customer> basic = repository.findByAccountType(AccountType.BASIC);
        List<Customer> premium = repository.findByAccountType(AccountType.PREMIUM);

        //then
        assertThat(basic, containsInAnyOrder(customer1,customer6,customer7));
        assertThat(premium, containsInAnyOrder(customer2,customer3,customer4,customer5));

//        List<Customer> expectedBasic = Arrays.asList(customer1, customer6, customer7);
//        List<Customer> expectedPremium = Arrays.asList(customer2, customer3, customer4, customer5);
//
//        assertTrue(
//                expectedBasic.size() == basic.size()
//                && expectedBasic.containsAll(basic)
//                && basic.containsAll(expectedBasic)
//        );
//        assertTrue(
//                expectedPremium.size() == premium.size()
//                        && expectedPremium.containsAll(premium)
//                        && premium.containsAll(expectedPremium)
//        );
    }

    @Test
    public void findByAccountTypeNullReturnsEmptyList(){
        //given
        setupCommonTestdata();

        //when
        List<Customer> result = repository.findByAccountType(null);

        //then
        assertThat(result, is(empty()));
    }


}
