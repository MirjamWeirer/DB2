package at.campus02.dbp2.relations;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OneToOneTests {

    @Test
    public void justATest(){
        //given
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnitName");
        EntityManager manager = factory.createEntityManager();
        Student student = new Student("Jasi");
        Animal animal = new Animal("Ivan");

        //when
        manager.getTransaction().begin();
        manager.persist(student);
        manager.persist(animal);
        manager.getTransaction().commit();
    }
}
