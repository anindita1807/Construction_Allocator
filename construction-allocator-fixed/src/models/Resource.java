package src.models;

public abstract class Resource implements Allocatable, Reportable {
    private String resourceId;
    private String name;
    private ResourceType type;
    private boolean available;
    private String allocatedTaskId;
    private int totalAllocations;

    public Resource(String resourceId, String name, ResourceType type) {
        this.resourceId = resourceId;
        this.name = name;
        this.type = type;
        this.available = true;
        this.allocatedTaskId = null;
        this.totalAllocations = 0;
    }

    // Allocatable interface implementation
    @Override
    public void allocate(String taskId) {
        this.available = false;
        this.allocatedTaskId = taskId;
        this.totalAllocations++;
    }

    @Override
    public void deallocate() {
        this.available = true;
        this.allocatedTaskId = null;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getAllocatedTaskId() {
        return allocatedTaskId;
    }

    // Abstract method — each subclass must implement this differently
    // This is polymorphism — same method, different behaviour
    public abstract double getCostPerHour();
    public abstract String getSpecification();

    // Getters — encapsulation
    public String getResourceId() { return resourceId; }
    public String getName() { return name; }
    public ResourceType getType() { return type; }
    public int getTotalAllocations() { return totalAllocations; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s",
            resourceId, name, type,
            available ? "AVAILABLE" : "ALLOCATED to " + allocatedTaskId);
    }
}

