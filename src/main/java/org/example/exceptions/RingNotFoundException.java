package org.example.exceptions;

public class RingNotFoundException extends RuntimeException {
    public RingNotFoundException() {
        super("Ring not found");
    }
    public RingNotFoundException(String message) {
        super(message);
    }
    public RingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RingNotFoundException(Throwable cause) {
        super(cause);
    }
}
