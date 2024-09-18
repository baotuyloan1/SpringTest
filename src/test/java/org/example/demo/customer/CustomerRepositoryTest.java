package org.example.demo.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"));

    @Autowired
    CustomerRepository underTest;

    @BeforeEach
    void setUp(){
        Customer customer = Customer.create("bao","nguyenducbao@gmail.com","US");
        underTest.save(customer);
    }

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void canEstablishedConnection(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldReturnCustomerWhenFindByEmail() {
        String email = "baonguyen@gmail.com";
        Customer customer = Customer.create("baonguyen", email,"US");
        underTest.save(customer);

        Optional<Customer> customerByEmail = underTest.findByEmail(email);
        assertThat(customerByEmail).isPresent();

    }

    @Test
    void shouldNotCustomerWhenFindByEmailIsNotPresent() {
        String email = "baonguyen1@gmail.com";
        Customer customer = Customer.create("baonguyen", email,"US");
        underTest.save(customer);

        Optional<Customer> customerByEmail = underTest.findByEmail("bao@gmail.com");
        assertThat(customerByEmail).isNotPresent();

    }
    @Test
    void shouldReturnCustomerWhenFindByEmail2() {
//        String email = "baonguyen1@gmail.com";
//        Customer customer = Customer.create("baonguyen", email,"US");
//        underTest.save(customer);

        Optional<Customer> customerByEmail = underTest.findByEmail("nguyenducbao@gmail.com");
        assertThat(customerByEmail).isPresent();

    }
}