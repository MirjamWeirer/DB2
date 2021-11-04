package at.campus02.dbp2.relations;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ManyToManyTests {
    //#region setup / tear down
    private EntityManagerFactory factory;
    private EntityManager manager;

    @BeforeEach
    public void setup(){
        factory = Persistence.createEntityManagerFactory("persistenceUnitName");
        manager = factory.createEntityManager();
    }

    @AfterEach
    public void teardown(){
        if (manager.isOpen())
            manager.close();
        if (factory.isOpen())
            factory.close();
    }
    //#endregion

    //#region many to many
    @Test
    public void manyToManyExample(){
        //given
        Animal cat = new Animal("cat");
        Animal elephant = new Animal("elephant");
        Animal tiger = new Animal("tiger");

        Country austria = new Country("Austria");
        Country india = new Country("India");
        Country southAfrica = new Country("SouthAfrica");

        //relations
        cat.getCountries().addAll(Arrays.asList(austria, india, southAfrica));
        elephant.getCountries().addAll(Arrays.asList(india, southAfrica));
        tiger.getCountries().add(india);

        //wegen cascade (PERSIST)
        austria.getAnimals().add(cat);
        india.getAnimals().addAll(Arrays.asList(cat, elephant, tiger));
        southAfrica.getAnimals().addAll(Arrays.asList(cat, elephant));

        //when
        manager.getTransaction().begin();
        manager.persist(austria);
        manager.persist(india);
        manager.persist(southAfrica);
        manager.getTransaction().commit();

        //then
        Country indiaFromDb = manager.find(Country.class, india.getName());
        manager.refresh(indiaFromDb);
        assertThat(indiaFromDb.getAnimals(), containsInAnyOrder(cat, tiger, elephant));

        Country southAfricaFromDb = manager.find(Country.class, southAfrica.getName());
        manager.refresh(southAfricaFromDb);
        assertThat(southAfricaFromDb.getAnimals(), containsInAnyOrder(cat, elephant));

        Country austriaFromDb = manager.find(Country.class, austria.getName());
        manager.refresh(austriaFromDb);
        assertThat(austriaFromDb.getAnimals(), contains(cat));

        Animal catFromDb = manager.find(Animal.class, cat.getId());
        manager.refresh(catFromDb);
        assertThat(catFromDb.getCountries(), containsInAnyOrder(austria, southAfrica, india));
    }


    //#endregion
}
