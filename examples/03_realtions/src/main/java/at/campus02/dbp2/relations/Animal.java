package at.campus02.dbp2.relations;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Animal {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @OneToOne
    private Student owner;
    @ManyToOne
    private Species species;

    //default constructor - im Fall von JPA definieren
    public Animal() {
    }

    public Animal(String name) {
        setName(name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student getOwner() {
        return owner;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id) && Objects.equals(name, animal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

