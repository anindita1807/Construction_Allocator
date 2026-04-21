package src.exceptions;

public class InvalidAllocationException extends Exception {
    public InvalidAllocationException(String message) {
        super("Invalid allocation: " + message);
    }
}

