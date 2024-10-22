package org.example.exceptions;

public class TechnologyNotFoundException extends RuntimeException {
    public TechnologyNotFoundException() {
        super("Technology not found");
    }
    public TechnologyNotFoundException(String message) {
        super(message);
    }
    public TechnologyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public TechnologyNotFoundException(Throwable cause) {
        super(cause);
    }
}
