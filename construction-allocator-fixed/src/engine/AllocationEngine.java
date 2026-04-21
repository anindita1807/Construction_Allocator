package src.engine;

import src.models.*;
import src.exceptions.*;

import java.util.*;

public class AllocationEngine {
    // HashMap for O(1) resource lookup — this is DSA
    private HashMap<String, Resource> resourceRegistry;

    // PriorityQueue for task scheduling by priority — this is DSA
    private PriorityQueue<Task> taskQueue;
    private HashMap<String, Task> activeTasks;

    // Stack for undo last allocation — this is DSA
    private Stack<Allocation> allocationHistory;

    // Counter for generating unique IDs
    private int allocationCounter;

    public AllocationEngine() {
        resourceRegistry = new HashMap<>();
        taskQueue = new PriorityQueue<>();
        activeTasks = new HashMap<>();
        allocationHistory = new Stack<>();
        allocationCounter = 1;
    }

    // ADD RESOURCE
    public void addResource(Resource resource) {
        resourceRegistry.put(resource.getResourceId(), resource);
        System.out.println("Resource added: " + resource.getName());
    }
    public void addTask(Task task) {
        taskQueue.offer(task);
        activeTasks.put(task.getTaskId(), task);
        System.out.println("Task added: " + task.getTaskName()
            + " with priority " + task.getPriority());
    }

    // ALLOCATE — main allocation logic
    public Allocation allocateResource(String resourceId, String taskId)
            throws ResourceNotFoundException, ResourceNotAvailableException,InvalidAllocationException {

        // Check resource exists
        if (!resourceRegistry.containsKey(resourceId)) {
            throw new ResourceNotFoundException(resourceId);
        }

        // Check task exists
        if (!activeTasks.containsKey(taskId)) {
            throw new InvalidAllocationException(
                "Task " + taskId + " does not exist or is not active.");
        }
        Resource resource = resourceRegistry.get(resourceId);
        Task task = activeTasks.get(taskId);

        // Check resource available
        if (!resource.isAvailable()) {
            throw new ResourceNotAvailableException(resourceId);
        }

        // Perform allocation
        resource.allocate(taskId);
        task.addResource(resourceId);
        task.addCost(resource.getCostPerHour() * task.getEstimatedHours());

        // Create allocation record
        String allocationId = "ALLOC-" + String.format("%03d", allocationCounter++);
        Allocation allocation = new Allocation(
            allocationId, resourceId, resource.getName(),
            taskId, task.getTaskName()
        );

        allocationHistory.push(allocation);
        System.out.println("SUCCESS: " + resource.getName()
            + " allocated to task " + task.getTaskName());
        return allocation;
    }

    // DEALLOCATE
    public void deallocateResource(String resourceId)
            throws ResourceNotFoundException {

        if (!resourceRegistry.containsKey(resourceId)) {
            throw new ResourceNotFoundException(resourceId);
        }

        Resource resource = resourceRegistry.get(resourceId);
        String taskId = resource.getAllocatedTaskId();

        if (resource.isAvailable()) {
            System.out.println("Resource " + resource.getName()
                + " is already available.");
            return;
        }

        // Remove from task
        if (taskId != null && activeTasks.containsKey(taskId)) {
            activeTasks.get(taskId).removeResource(resourceId);
        }

        resource.deallocate();

        // Mark latest allocation as completed
        for (int i = allocationHistory.size() - 1; i >= 0; i--) {
            Allocation a = allocationHistory.get(i);
            if (a.getResourceId().equals(resourceId)
                    && a.getStatus().equals("ACTIVE")) {
                a.complete();
                break;
            }
        }
        System.out.println("Resource " + resource.getName()
            + " deallocated successfully.");
    }

    // UNDO last allocation using Stack
    public void undoLastAllocation() throws ResourceNotFoundException {
        if (allocationHistory.isEmpty()) {
            System.out.println("No allocations to undo.");
            return;
        }

        // Find last active allocation
        Allocation last = null;
        for (int i = allocationHistory.size() - 1; i >= 0; i--) {
            if (allocationHistory.get(i).getStatus().equals("ACTIVE")) {
                last = allocationHistory.get(i);
                break;
            }
        }
        if (last == null) {
            System.out.println("No active allocations to undo.");
            return;
        }

        deallocateResource(last.getResourceId());
        System.out.println("Undo successful for allocation: "
            + last.getAllocationId());
    }

    // GET NEXT PRIORITY TASK
    public Task getNextPriorityTask() {
        if (taskQueue.isEmpty()) {
            System.out.println("No tasks in queue.");
            return null;
        }
        return taskQueue.peek();
    }

    public List<Resource> getAvailableResources() {
        List<Resource> available = new ArrayList<>();
        for (Resource r : resourceRegistry.values()) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    // GET AVAILABLE RESOURCES BY TYPE
    public List<Resource> getAvailableByType(ResourceType type) {
        List<Resource> available = new ArrayList<>();
        for (Resource r : resourceRegistry.values()) {
            if (r.isAvailable() && r.getType() == type) available.add(r);
        }
        return available;
    }

    // GET ALL RESOURCES
    public Collection<Resource> getAllResources() {
        return resourceRegistry.values();
    }

    // GET ALL TASKS
    public Collection<Task> getAllTasks() {
        return activeTasks.values();
    }

    // GET ALLOCATION HISTORY
    public Stack<Allocation> getAllocationHistory() {
        return allocationHistory;
    }

    // COMPLETE TASK
    public void completeTask(String taskId) {
        if (!activeTasks.containsKey(taskId)) {
            System.out.println("Task not found: " + taskId);
            return;
        }
        Task task = activeTasks.get(taskId);

        // Deallocate all resources from this task
        for (String resourceId : new ArrayList<>(task.getAllocatedResourceIds())) {
            try {
                deallocateResource(resourceId);
            } catch (ResourceNotFoundException e) {
                System.out.println("Warning: " + e.getMessage());
            }
        }

        task.setStatus("COMPLETED");
        System.out.println("Task " + task.getTaskName() + " marked as COMPLETED.");
    }

    // GENERATE FULL REPORT
    public String generateFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========================================\n");
        sb.append("   CONSTRUCTION SITE RESOURCE REPORT\n");
        sb.append("========================================\n\n");

        // Resource summary
        int totalResources = resourceRegistry.size();
        long availableCount = resourceRegistry.values()
            .stream().filter(Resource::isAvailable).count();

        sb.append(String.format("Total Resources  : %d\n", totalResources));
        sb.append(String.format("Available        : %d\n", availableCount));
        sb.append(String.format("Allocated        : %d\n",
            totalResources - availableCount));
        sb.append(String.format("Total Tasks      : %d\n", activeTasks.size()));
        sb.append(String.format("Total Allocations: %d\n\n",
            allocationHistory.size()));

        // Task wise summary
        sb.append("--- TASK SUMMARY ---\n");
        for (Task t : activeTasks.values()) {
            sb.append(t.toString()).append("\n");
        }

        // Allocation history
        sb.append("\n--- ALLOCATION HISTORY ---\n");
        if (allocationHistory.isEmpty()) {
            sb.append("No allocations made yet.\n");
        } else {
            for (Allocation a : allocationHistory) {
                sb.append(a.toString()).append("\n");
            }
        }

        sb.append("\n========================================\n");
        return sb.toString();
    }

    // SEARCH resource by name
    public List<Resource> searchByName(String keyword) {
        List<Resource> results = new ArrayList<>();
        for (Resource r : resourceRegistry.values()) {
            if (r.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }
}





