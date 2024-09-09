import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import testcontainer.Customer;
import testcontainer.CustomerService;
import testcontainer.DBConnectionProvider;

public class CustomerServiceTest {
    // actual db but started in the testContainer enviroment
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    CustomerService customerService;

    // start the db
    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    // set up a connection with the db
    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        customerService = new CustomerService(connectionProvider); // creates a new table if one doesn't exist
    }

    // insert a new customer via the createCustomer method
    @Test
    void shouldGetCustomers() {
        customerService.createCustomer(new Customer(1L, "George"));
        customerService.createCustomer(new Customer(2L, "John"));

        // retrieve a list of customers
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }
}
