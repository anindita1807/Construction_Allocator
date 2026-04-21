package src.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String resourceId) {
        super("Resource with ID " + resourceId + " not found in the system.");
    }
}
