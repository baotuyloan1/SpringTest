package org.example.demo.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository underTest;

    @Test
    void shouldReturnCustomerWhenFindByEmail() {
        String email = "baonguyen@gmail.com";
        Customer customer = Customer.create("baonguyen", email,"US");
        underTest.save(customer);

        Optional<Customer> customerByEmail = underTest.findByEmail(email);
        assertThat(customerByEmail).isPresent();

    }
}