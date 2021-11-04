package at.campus02.dbp2.assignment;

import java.util.Objects;

public class Customer {

    public Customer() {
    }

    public Customer(String email) {
    }

    public String getEmail() {
        return null;
    }

    public String getFirstname() {
        return null;
    }

    public void setFirstname(String firstname) {
    }

    public String getLastname() {
        return null;
    }

    public void setLastname(String lastname) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getEmail(), customer.getEmail()) && Objects.equals(getFirstname(), customer.getFirstname()) && Objects.equals(getLastname(), customer.getLastname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getFirstname(), getLastname());
    }
}
