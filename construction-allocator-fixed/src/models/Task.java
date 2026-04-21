package src.models;

import java.util.ArrayList;
import java.util.List;

public class Task implements Comparable<Task> {
    private String taskId;
    private String taskName;
    private String description;
    private TaskPriority priority;
    private String status;
    private List<String> allocatedResourceIds;
    private double estimatedHours;
    private double estimatedCost;

    public Task(String taskId, String taskName, String description,
                TaskPriority priority, double estimatedHours) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.priority = priority;
        this.estimatedHours = estimatedHours;
        this.status = "PENDING";
        this.allocatedResourceIds = new ArrayList<>();
        this.estimatedCost = 0;
    }

    // compareTo for PriorityQueue — higher priority comes first
    @Override
    public int compareTo(Task other) {
        return Integer.compare(
            other.priority.getLevel(),
            this.priority.getLevel()
        );
    }

    public void addResource(String resourceId) {
        allocatedResourceIds.add(resourceId);
    }
    public void removeResource(String resourceId) {
        allocatedResourceIds.remove(resourceId);
    }

    public void addCost(double cost) {
        this.estimatedCost += cost;
    }

    public void setStatus(String status) { this.status = status; }

    public String getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public String getDescription() { return description; }
    public TaskPriority getPriority() { return priority; }
    public String getStatus() { return status; }
    public List<String> getAllocatedResourceIds() { return allocatedResourceIds; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getEstimatedCost() { return estimatedCost; }

    @Override
    public String toString() {
        return String.format(
             "[%s] %s | Priority: %s | Status: %s | Resources: %d | Est. Cost: Rs %.0f",
            taskId, taskName, priority, status,
            allocatedResourceIds.size(), estimatedCost
        );
    }
}
