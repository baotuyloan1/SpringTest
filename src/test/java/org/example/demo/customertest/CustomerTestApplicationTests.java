package org.example.demo.customertest;

import org.example.demo.customer.Customer;
import org.example.demo.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerTestApplicationTests {
    @Autowired
    CustomerRepository underTest;

    @Test
    void shouldReturnCustomerWhenFindByEmail() {
        //give
        String email = "baonguyen@gmail.com";
        Customer customer = Customer.create("baonguyen", email,"US");
        underTest.save(customer);

        //when
        Optional<Customer> customerByEmail = underTest.findByEmail(email);
        assertThat(customerByEmail).isPresent();

    }

}
