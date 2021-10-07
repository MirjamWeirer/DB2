package at.campus02.dbp2.repository;

public class Application {

    public static void log(String msg) {
        System.out.println("Application:  --> " + msg);
    }

    public static void main(String[] args) {
        log("application started");

        CustomerRepository repository = new JdbcRepository("jdbc:derby:database;create=true");

        Customer customer = new Customer();
        customer.setEmail("customer1@customer.com");
        customer.setLastname("Customer");
        customer.setFirstname("Miri");

        //1.) Create
        repository.create(customer);
        log("Customer created: " + customer);

        //2.) Read
        Customer fromRepository = repository.read(customer.getEmail());
        log("Customer read" + fromRepository);

        //3.) Update
        fromRepository.setFirstname("Mirjam");
        repository.update(fromRepository);
        Customer updated = repository.read(fromRepository.getEmail());
        log("Customer updated: " + updated);

        //4.) Delete
        repository.delete(updated);
        Customer deleted = repository.read(updated.getEmail());
        log("Customer deleted: " + deleted);
    }
}
