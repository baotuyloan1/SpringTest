package org.example.demo.customer;

import org.assertj.core.api.Assertions;
import org.example.demo.exception.CustomerEmailUnavailableException;
import org.example.demo.exception.CustomerNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension .class)
class CustomerServiceTest {

    CustomerService underTest;


    @Mock
    CustomerRepository customerRepository;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldGetAllCustomers() {

        underTest.getCustomers();;
        verify(customerRepository).findAll();
    }


    @Test
    void shouldCreateCustomer() {
        //given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                "bao",
                "nguyenducbao@gmail.com",
                "VN");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        //when
        underTest.createCustomer(createCustomerRequest);
        //then
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(createCustomerRequest.name());
        assertThat(customerCaptured.getEmail()).isEqualTo(createCustomerRequest.email());
        assertThat(customerCaptured.getAddress()).isEqualTo(createCustomerRequest.address() );
    }

    @Test
    void shouldNotCreateCustomerAndThrowExceptionWhenCustomerFindByEmailIsPresent() {
        //given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                "bao",
                "nguyenducbao@gmail.com",
                "VN");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(new Customer()));
        //when

        //then
       assertThatThrownBy(() ->
               underTest.createCustomer(createCustomerRequest))
               .isInstanceOf(CustomerEmailUnavailableException.class)
               .hasMessageContaining("The email " + createCustomerRequest.email() + " unavailable.");
       verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldOnlyUpdateCustomerName(){
        //given
        long id = 5L;
        String newName = "newBao";
        Customer customer =  Customer.create(id,"bao","nguyenducbao@gmail.com","VN");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,newName, null, null);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(newName);
        assertThat(customerCaptured.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCaptured.getAddress()).isEqualTo(customer.getAddress());
        assertThat(customerCaptured.getId()).isEqualTo(id);
        //then
    }

    @Test
    void shouldThrowNotFoundWhenGivenInvalidIDWhileUpdateCustomer() {
        long id = 5L;
        String name = "Bao";
        String email = "nguyenducbao@gmail.com";
        String address = "VN";

        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()->
                underTest.updateCustomer(id, name, email, address))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer with id " + id + " doesn't found");
        verify(customerRepository,never()).save(any());
    }

    @Test
    void shouldThrowEmailUnavailableWhenGiveEmailAlreadyPresentedWhileUpdateCustomer(){
        long id = 5L;
        String newEmail = "nguyenducbaonew@gmail.com";
        Customer customer =  Customer.create(id,"Bao","nguyenducbao@gmail.com","VN");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail(newEmail)).thenReturn(Optional.of(new Customer()));

        assertThatThrownBy(() ->
                underTest.updateCustomer(id,null,newEmail,null))
                .isInstanceOf(CustomerEmailUnavailableException.class)
                .hasMessageContaining("The email " + newEmail + " unavailable to update");
        verify(customerRepository,never()).save(any());
    }

    @Test
    void shouldUpdateOnlyCustomerEmail(){
        //given
        long id = 5L;
        String newEmail = "nguyenducbaonew@gmail.com";
        Customer customer =  Customer.create(id,"bao","nguyenducbao@gmail.com","VN");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,null, newEmail, null);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(customer.getName());
        assertThat(customerCaptured.getEmail()).isEqualTo(newEmail);
        assertThat(customerCaptured.getAddress()).isEqualTo(customer.getAddress());
        assertThat(customerCaptured.getId()).isEqualTo(id);
    }

    @Test
    void shouldUpdateOnlyCustomerAddress(){
        //given
        long id = 5L;
        String newAddress = "NYC";
        Customer customer =  Customer.create(id,"bao","nguyenducbao@gmail.com","VN");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,null, null, newAddress);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(customer.getName());
        assertThat(customerCaptured.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCaptured.getAddress()).isEqualTo(newAddress);
        assertThat(customerCaptured.getId()).isEqualTo(id);

    }

    @Test
    void shouldUpdateAllAttributeWhenUpdateCustomer(){
        long id = 5L;
        String newAddress = "NYC";
        String newName = "baonew";
        String newEmail = "nguyenducbaonew@gmail.com";
        Customer customer =  Customer.create(id,"bao","nguyenducbao@gmail.com","VN");
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //when
        underTest.updateCustomer(id,newName, newEmail, newAddress);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(newName);
        assertThat(customerCaptured.getEmail()).isEqualTo(newEmail);
        assertThat(customerCaptured.getAddress()).isEqualTo(newAddress);
        assertThat(customerCaptured.getId()).isEqualTo(id);

    }

    @Test
    void shouldThrowNotFoundWhenGivenIdDoseExistWhileDeleteCustomer() {
        long id = 5L;
        when(customerRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(()->
                underTest.deleteCustomer(id))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer with id " + id + " doesn't exist.");

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void shouldDeleteCustomer(){
        long id = 5L;
        when(customerRepository.existsById(id)).thenReturn(true);

        underTest.deleteCustomer(id);

        verify(customerRepository).deleteById(id);
    }
}