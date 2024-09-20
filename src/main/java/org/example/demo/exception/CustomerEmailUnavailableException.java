package org.example.demo.exception;

public class CustomerEmailUnavailableException extends RuntimeException{


    public CustomerEmailUnavailableException(String message) {
        super(message);
    }
}
