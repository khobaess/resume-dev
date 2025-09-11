package com.example.resumedev.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class VkConnectionException extends RuntimeException{

    public VkConnectionException(String message) {
        super(message);
    }

    public VkConnectionException(String message, Exception e) {
        super(message, e);
    }

    public VkConnectionException(String message, WebClientException cause) {
        super(message, cause);
    }
}
