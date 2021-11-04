package at.campus02.dbp2.assignment;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment {

    public Integer getId() {
        return null;
    }

    public Customer getCustomer() {
        return null;
    }

    public void setCustomer(Customer customer) {
    }

    public Provider getProvider() {
        return null;
    }

    public void setProvider(Provider provider) {
    }

    public LocalDateTime getTime() {
        return null;
    }

    public void setTime(LocalDateTime time) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTime(), that.getTime()) && Objects.equals(getCustomer(), that.getCustomer()) && Objects.equals(getProvider(), that.getProvider());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTime(), getCustomer(), getProvider());
    }

}
