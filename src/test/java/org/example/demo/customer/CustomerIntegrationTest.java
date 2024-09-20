package org.example.demo.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.OK;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    public static final String API_CUSTOMER_PATH = "/api/v1/customers";
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2"));

    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp() {

    }

    @Test
    void canEstablishedConnection(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
    @Test
    void shouldCreateCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "bao",
                "nguyenducbao"+ UUID.randomUUID()+"@gmail.com",
                "VN");

        ResponseEntity<Void> createCustomerResponse = testRestTemplate.exchange(
                API_CUSTOMER_PATH,
                POST,
                new HttpEntity<>(request),
                Void.class);

        assertThat(createCustomerResponse.getStatusCode())
                .isEqualTo(OK);

        ResponseEntity<List<Customer>> allCustomersResponse =  testRestTemplate.exchange(
                API_CUSTOMER_PATH,
                GET,
                null,
                new ParameterizedTypeReference<>(){});
        assertThat(allCustomersResponse.getStatusCode()).isEqualTo(OK);
        Customer customerCreated = allCustomersResponse.getBody()
                .stream()
                .filter(c -> c.getEmail().equals(request.email()))
                .findFirst()
                .orElseThrow();

        assertThat(customerCreated.getName()).isEqualTo(request.name());
        assertThat(customerCreated.getAddress()).isEqualTo(request.address());
        assertThat(customerCreated.getEmail()).isEqualTo(request.email());
    }

    @Test
    void shouldUpdateCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "bao",
                "nguyenducbao"+ UUID.randomUUID()+"@gmail.com",
                "VN");

        ResponseEntity<Void> createCustomerResponse = testRestTemplate.exchange(
                API_CUSTOMER_PATH,
                POST,
                new HttpEntity<>(request),
                Void.class);

        assertThat(createCustomerResponse.getStatusCode())
                .isEqualTo(OK);

        ResponseEntity<List<Customer>> allCustomersResponse =  testRestTemplate.exchange(
                API_CUSTOMER_PATH,
                GET,
                null,
                new ParameterizedTypeReference<>(){});
        assertThat(allCustomersResponse.getStatusCode()).isEqualTo(OK);

        Customer customer = allCustomersResponse.getBody().stream().filter(c -> c.getEmail().equals(request.email())).findFirst().orElseThrow();

        Long id = customer.getId();
        String newEmail = "newEmail"+ UUID.randomUUID()+"@gmail.com";
        testRestTemplate.exchange(
                API_CUSTOMER_PATH+"/"+id+"?email="+newEmail,
                PUT,
                null,
                Void.class).getStatusCode().is2xxSuccessful();

        ResponseEntity<Customer> customerByIdResponse = testRestTemplate.exchange(
                API_CUSTOMER_PATH + "/" + id,
                GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(customerByIdResponse.getStatusCode()).isEqualTo(OK);

        Customer customerUpdated = Objects.requireNonNull(customerByIdResponse.getBody());

        assertThat(customerUpdated.getEmail()).isNotEqualTo(request.email());
        assertThat(customerUpdated.getEmail()).isEqualTo(newEmail);
        assertThat(customerUpdated.getAddress()).isEqualTo(request.address());
        assertThat(customerUpdated.getName()).isEqualTo(request.name());

    }

    @Test
    void deleteCustomer() {
    }
}