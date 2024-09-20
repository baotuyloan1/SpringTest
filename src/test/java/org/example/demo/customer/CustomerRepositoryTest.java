package org.example.demo.customer;

import org.example.demo.AbstractTestContainerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainerTest {



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