package at.campus02.dbp2.relations;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class OneToManyTests {
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

    //#region: persist with cascade
    @Test
    public void persistWithCascade(){
        //given
        Species species = new Species("Mammals");
        Animal bunny = new Animal("Bunny");
        Animal ivan = new Animal("Ivan");

        //Referenzen für FK in der DB
        bunny.setSpecies(species);
        ivan.setSpecies(species);
        //Referenzen für Cascade
        species.getAnimals().add(bunny);
        species.getAnimals().add(ivan);

        //when
        manager.getTransaction().begin();
        manager.persist(species);
        manager.getTransaction().commit();

        manager.clear();

        //then
        Species speciesFromDb = manager.find(Species.class, species.getId());
        assertThat(speciesFromDb.getAnimals().size(), is(2));
        assertThat(speciesFromDb.getAnimals(), containsInAnyOrder(bunny,ivan));
    }
    //#endregion

    @Test
    @Disabled("Only works without orphanRemoval - enable after setting orphanRemoval to false")
    public void updateExamExampleWithCorrectingReferences(){
        //--------------------------------------
        //given
        Animal clownfish = new Animal("Nemo");
        Animal squirrel = new Animal("Squirrel");
        Species fish = new Species("Fish");

        //Referenzen für DB
        clownfish.setSpecies(fish);
        //Fehler -> korrigieren
        squirrel.setSpecies(fish);

        //Referenzen für Cascade
        fish.getAnimals().add(clownfish);
        fish.getAnimals().add(squirrel);

        //Speichern
        manager.getTransaction().begin();
        manager.persist(fish);
        manager.getTransaction().commit();

        manager.clear();
        //--------------------------------------
        //when: Korrekturversuch, zum Scheitern verurteilt ...
        manager.getTransaction().begin();
        fish.getAnimals().remove(squirrel);
        manager.merge(fish);
        manager.getTransaction().commit();
        manager.clear();
        //--------------------------------------
        //then
        //Squirrel existiert noch in DB
        Animal squirrelFromDb = manager.find(Animal.class, squirrel.getId());
        assertThat(squirrelFromDb,is(notNullValue()));

        // Squirrel ist immer noch Fisch - wir haben im Speicher die Liste von
        //"Fisch" geändert, aber species von Squirrel zeigt nach wie vor auf "Fisch"
        //auch in der DB.
        assertThat(squirrelFromDb.getSpecies().getId(),is(fish.getId()));

        //auch wenn wir die Liste mittels "refresh" neue einlesen, wird die
        //Referenz von Squirrel auf Fish (DB) neu eingelesen und Squirrel ist
        //wieder in der Liste drin.
        Species mergedFish = manager.merge(fish);
        manager.refresh(mergedFish);
        assertThat(mergedFish.getAnimals().size(),is(2));

        //--------------------------------------
        //when: Korrekturversuch, diesmal richtig ...
        manager.getTransaction().begin();
        squirrel.setSpecies(null);
        manager.merge(squirrel);
        manager.getTransaction().commit();
        manager.clear();
        //--------------------------------------
        //then
        //Squirrel existiert noch in DB
        squirrelFromDb = manager.find(Animal.class, squirrel.getId());
        assertThat(squirrelFromDb,is(notNullValue()));

        // Squirrel ist kein Fisch mehr
        assertThat(squirrelFromDb.getSpecies(),is(nullValue()));

        //auch wenn wir die Liste mittels "refresh" neue einlesen, wird die
        //Referenz von Squirrel auf Fish (DB) neu eingelesen und Squirrel ist
        //wieder in der Liste drin.
        mergedFish = manager.merge(fish);
        manager.refresh(mergedFish);
        assertThat(mergedFish.getAnimals().size(),is(1));
    }

    @Test
    public void orphanRemovalDeletesOrphansFromDatabase(){
        //given
        Animal clownfish = new Animal("Nemo");
        Animal squirrel = new Animal("Squirrel");
        Species fish = new Species("Fish");

        //Referenzen für DB
        clownfish.setSpecies(fish);
        //Fehler -> korrigieren
        squirrel.setSpecies(fish);

        //Referenzen für Cascade
        fish.getAnimals().add(clownfish);
        fish.getAnimals().add(squirrel);

        //Speichern
        manager.getTransaction().begin();
        manager.persist(fish);
        manager.getTransaction().commit();

        manager.clear();
        //--------------------------------------
        //when
        manager.getTransaction().begin();
        fish.getAnimals().remove(squirrel);
        manager.merge(fish);
        manager.getTransaction().commit();

        manager.clear();

        //then
        Animal squirrelFromDb = manager.find(Animal.class, squirrel.getId());
        assertThat(squirrelFromDb, is(nullValue()));

        Species refreshedFish = manager.merge(fish);
        manager.refresh(refreshedFish);
        assertThat(refreshedFish.getAnimals().size(),is(1));
    }
}
