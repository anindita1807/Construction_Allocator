package src.exceptions;

public class ResourceNotAvailableException extends Exception {
    private String resourceId;

    public ResourceNotAvailableException(String resourceId) {
        super("Resource " + resourceId + " is not available — already allocated to another task.");
        this.resourceId = resourceId;
    }

    public String getResourceId() { return resourceId; }
}
