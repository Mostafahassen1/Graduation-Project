package com.codemeet.utils.exception;

public class DuplicateResourceException extends RuntimeException {
    
    private final ResourceType resource;
    
    public DuplicateResourceException(String message, ResourceType resource) {
        super(message);
        this.resource = resource;
    }
    
    public ResourceType getResource() {
        return resource;
    }
}
