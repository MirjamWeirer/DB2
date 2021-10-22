package at.campus02.dbp2.relations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;


public class OneToOneTests {
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

    //#region persist and relation
    @Test
    public void persistAnimalAndStudentStoresRelationInDatabase(){
        //given
        Student student = new Student("Jasi");
        Animal animal = new Animal("Ivan");

        //im Speicher selber um die Referenzen kümmern
        student.setPet(animal);
        animal.setOwner(student);

        //when
        manager.getTransaction().begin();
        manager.persist(student);
        manager.persist(animal);
        manager.getTransaction().commit();

        manager.clear();

        //then
        Animal ivanFromDb = manager.find(Animal.class, animal.getId());
        assertThat(ivanFromDb.getOwner(), is(student));

        Student ownerFromDb = manager.find(Student.class, student.getId());
        assertThat(ownerFromDb.getPet(), is(animal));
    }
    //#endregion

    //#region persists with cascade
    @Test
    public void persisStudentWithCascadeAlsoPersistsAnimal(){
        //given
        Student natali = new Student("Natali");
        Animal bunny = new Animal("Bunny");

        //Referenzen im Speicher verwalten:
        //1) Owner setzen, um in der DB die Relation zu schließen
        bunny.setOwner(natali);
        //2) Pet setzen, damit CASCADE funktioniert
        natali.setPet(bunny);

        //when
        manager.getTransaction().begin();
        manager.persist(natali);
        //"bunny" soll durch cascade mit "natali" mitgespeichert werden
        manager.getTransaction().commit();

        manager.clear();

        //then
        Animal bunnyFromDB = manager.find(Animal.class, bunny.getId());
        assertThat(bunnyFromDB.getOwner(), is(natali));

        Student nataliFromDb = manager.find(Student.class, natali.getId());
        assertThat(nataliFromDb.getPet(), is(bunny));
    }
    //#endregion

    //#region references not handled in memory
    @Test
    public void refreshClosesReferencesNotHandledInMemory(){
        //given
        Student natali = new Student("Natali");
        Animal bunny = new Animal("Bunny");

        //Referenzen im Speicher verwalten:
        //1) Owner setzen, um in der DB die Relation zu schließen
        bunny.setOwner(natali);
        //2) Pet absichtlich nicht setzen, um refresh zu demonstrieren
        //natali.setPet(bunny);

        //when
        manager.getTransaction().begin();
        manager.persist(bunny);
        //nachdem an "natali" kein Pet gesetzt ist, reicht es nicht, "natali" allein zu persistieren
        //(Cascade kann nicht greifen).
        //d.h. wir müssen beide Entities persistieren (Reihenfolge
        //innerhalb der Transaktion ist egal).
        manager.persist(natali);
        manager.getTransaction().commit();

        manager.clear();
        //then
        //1) Referenz von Animal auf Student ist gesetzt
        Animal bunnyFromDb = manager.find(Animal.class, bunny.getId());
        assertThat(bunnyFromDb.getOwner(),is(natali));
        //2) ohne refresh wird die Referenz von "natali" auf "bunny" nicht geschlossen
        //nicht geschlossen (auch nicht nach manager.clear(), welches den
        //Level1 Cache leert - bei Relationen).
        Student nataliFromDb = manager.find(Student.class,natali.getId());
        assertThat(nataliFromDb.getPet(),is(nullValue()));
        //3) "refresh" erzwingt das Neu-Einlesen aus der DB
        //auch mit Relationen.
        manager.refresh(nataliFromDb);
        assertThat(nataliFromDb.getPet(),is(bunny));
    }
    //#endregion
}
