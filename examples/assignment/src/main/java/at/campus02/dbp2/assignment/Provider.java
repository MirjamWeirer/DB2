package at.campus02.dbp2.assignment;

import java.util.List;
import java.util.Objects;

public class Provider {

    public Integer getId() {
        return null;
    }

    public ProviderType getType() {
        return null;
    }

    public void setType(ProviderType type) {
    }

    public String getAddress() {
        return null;
    }

    public void setAddress(String address) {
    }

    public List<Appointment> getAppointments() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Objects.equals(getId(), provider.getId()) && getType() == provider.getType() && Objects.equals(getAddress(), provider.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getAddress());
    }
}
