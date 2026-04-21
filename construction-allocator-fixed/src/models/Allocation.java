package src.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Allocation {
    private String allocationId;
    private String resourceId;
    private String resourceName;
    private String taskId;
    private String taskName;
    private LocalDateTime allocatedAt;
    private LocalDateTime deallocatedAt;
    private String status;

    public Allocation(String allocationId, String resourceId,
                      String resourceName, String taskId, String taskName) {
        this.allocationId = allocationId;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.taskId = taskId;
        this.taskName = taskName;
        this.allocatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public void complete() {
        this.deallocatedAt = LocalDateTime.now();
        this.status = "COMPLETED";
    }

    public String getAllocationId() { return allocationId; }
    public String getResourceId() { return resourceId; }
    public String getTaskId() { return taskId; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format(
            "[%s] Resource: %s -> Task: %s | Allocated: %s | Status: %s",
            allocationId, resourceName, taskName,
            allocatedAt.format(fmt), status
        );
    }
}
