package src.models;

public interface Allocatable {
    void allocate(String taskId);
    void deallocate();
    boolean isAvailable();
    String getAllocatedTaskId();
}