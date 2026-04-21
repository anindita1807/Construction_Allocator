package src.ui;

import src.engine.AllocationEngine;
import src.exceptions.*;
import src.models.*;

import java.util.*;

public class ConsoleUI {
    private AllocationEngine engine;
    private Scanner scanner;

    // column widths for resource table
    private static final int W_ID   = 8;
    private static final int W_NAME = 20;
    private static final int W_TYPE = 10;
    private static final int W_SPEC = 38;
    private static final int W_STAT = 20;

    // column widths for task table
    private static final int T_ID   = 6;
    private static final int T_NAME = 22;
    private static final int T_PRI  = 10;
    private static final int T_STAT = 11;
    private static final int T_RES  = 5;
    private static final int T_COST = 14;

    // column widths for allocation history table
    private static final int A_ID   = 12;
    private static final int A_RES  = 18;
    private static final int A_TASK = 22;
    private static final int A_TIME = 17;
    private static final int A_STAT = 10;

    public ConsoleUI() {
        engine  = new AllocationEngine();
        scanner = new Scanner(System.in);
        loadSampleData();
    }

    // Load realistic construction site data (unchanged)
    private void loadSampleData() {
        System.out.println("Loading construction site data...\n");

        engine.addResource(new HumanResource("HR001", "Rajesh Kumar",   "Civil Engineer",   8,  2500));
        engine.addResource(new HumanResource("HR002", "Amit Singh",     "Electrician",      5,  1800));
        engine.addResource(new HumanResource("HR003", "Suresh Patil",   "Laborer",          2,   800));
        engine.addResource(new HumanResource("HR004", "Vikram Rao",     "Plumber",          6,  1600));
        engine.addResource(new HumanResource("HR005", "Priya Sharma",   "Site Supervisor", 10,  3000));

        engine.addResource(new EquipmentResource("EQ001", "Tower Crane",   "Crane",     50, 5000, true));
        engine.addResource(new EquipmentResource("EQ002", "JCB Excavator", "Excavator", 30, 3500, true));
        engine.addResource(new EquipmentResource("EQ003", "Cement Mixer",  "Mixer",      5,  800, false));
        engine.addResource(new EquipmentResource("EQ004", "Concrete Pump", "Pump",      20, 2500, true));

        engine.addResource(new MaterialResource("MT001", "Steel Rods",  "tons",   100.0, 65000));
        engine.addResource(new MaterialResource("MT002", "Cement Bags", "bags",   500.0,   380));
        engine.addResource(new MaterialResource("MT003", "Red Bricks",  "units", 10000.0,    8));

        engine.addTask(new Task("T001", "Foundation Work",       "Dig and lay foundation for Block A", TaskPriority.CRITICAL, 48.0));
        engine.addTask(new Task("T002", "Electrical Wiring",     "Complete wiring for floors 1 to 3",  TaskPriority.HIGH,     24.0));
        engine.addTask(new Task("T003", "Plumbing Installation", "Install pipes and drainage system",  TaskPriority.MEDIUM,   16.0));
        engine.addTask(new Task("T004", "Roofing",               "Lay roof slabs for Block A",         TaskPriority.LOW,      32.0));
        engine.addTask(new Task("T005", "Exterior Painting",     "Paint outer walls of Block A",       TaskPriority.LOW,      20.0));

        System.out.println("\nSample data loaded successfully!");
        System.out.println("12 resources and 5 tasks ready.\n");
    }

    // ── start / menu ───────────────────────────────────────────────
    public void start() {
        printBanner("CONSTRUCTION SITE RESOURCE ALLOCATOR", "Powered by Java OOP + DSA");
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("  Enter choice: ");
            System.out.println();
            switch (choice) {
                case 1  -> viewAllResources();
                case 2  -> viewAvailableResources();
                case 3  -> viewAllTasks();
                case 4  -> allocateResource();
                case 5  -> deallocateResource();
                case 6  -> viewNextPriorityTask();
                case 7  -> completeTask();
                case 8  -> undoLastAllocation();
                case 9  -> viewAllocationHistory();
                case 10 -> viewResourceReport();
                case 11 -> searchResource();
                case 12 -> generateFullReport();
                case 0  -> {
                    System.out.println("  Thank you for using AniCura Construction Allocator. Goodbye!\n");
                    running = false;
                }
                default -> System.out.println("  [!] Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("  +==================================+");
        System.out.println("  |           MAIN  MENU            |");
        System.out.println("  +==================================+");
        System.out.println("  |  1.  View All Resources         |");
        System.out.println("  |  2.  View Available Resources   |");
        System.out.println("  |  3.  View All Tasks             |");
        System.out.println("  |  4.  Allocate Resource to Task  |");
        System.out.println("  |  5.  Deallocate Resource        |");
        System.out.println("  |  6.  View Next Priority Task    |");
        System.out.println("  |  7.  Complete a Task            |");
        System.out.println("  |  8.  Undo Last Allocation       |");
        System.out.println("  |  9.  View Allocation History    |");
        System.out.println("  |  10. View Resource Report       |");
        System.out.println("  |  11. Search Resource by Name    |");
        System.out.println("  |  12. Generate Full Site Report  |");
        System.out.println("  |  0.  Exit                       |");
        System.out.println("  +==================================+");
    }

    // ── option 1 : all resources ───────────────────────────────────
    private void viewAllResources() {
        printSectionHeader("ALL RESOURCES");
        printResourceTableHeader();
        for (Resource r : engine.getAllResources()) {
            printResourceRow(r);
        }
        printResourceTableFooter();
    }

    // ── option 2 : available resources ────────────────────────────
    private void viewAvailableResources() {
        printSectionHeader("AVAILABLE RESOURCES");
        List<Resource> available = engine.getAvailableResources();
        if (available.isEmpty()) {
            System.out.println("  No resources are currently available.\n");
            return;
        }
        printResourceTableHeader();
        for (Resource r : available) {
            printResourceRow(r);
        }
        printResourceTableFooter();
    }

    // ── option 3 : all tasks ──────────────────────────────────────
    private void viewAllTasks() {
        printSectionHeader("ALL TASKS");
        printTaskTableHeader();
        for (Task t : engine.getAllTasks()) {
            printTaskRow(t);
        }
        printTaskTableFooter();
    }

    // ── option 4 : allocate ───────────────────────────────────────
    private void allocateResource() {
        printSectionHeader("ALLOCATE RESOURCE TO TASK");
        System.out.print("  Resource ID (e.g. HR001, EQ001, MT001) : ");
        String resourceId = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Task ID     (e.g. T001, T002)          : ");
        String taskId = scanner.nextLine().trim().toUpperCase();
        System.out.println();
        try {
            engine.allocateResource(resourceId, taskId);
        } catch (ResourceNotFoundException | ResourceNotAvailableException | InvalidAllocationException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
        System.out.println();
    }

    // ── option 5 : deallocate ─────────────────────────────────────
    private void deallocateResource() {
        printSectionHeader("DEALLOCATE RESOURCE");
        System.out.print("  Resource ID to deallocate : ");
        String resourceId = scanner.nextLine().trim().toUpperCase();
        System.out.println();
        try {
            engine.deallocateResource(resourceId);
        } catch (ResourceNotFoundException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
        System.out.println();
    }

    // ── option 6 : next priority task ─────────────────────────────
    private void viewNextPriorityTask() {
        printSectionHeader("NEXT PRIORITY TASK");
        Task next = engine.getNextPriorityTask();
        if (next == null) return;

        System.out.println("  +--------------------------------------------------+");
        System.out.printf ("  |  %-12s : %-33s|%n", "Task ID",     next.getTaskId());
        System.out.printf ("  |  %-12s : %-33s|%n", "Name",        trunc(next.getTaskName(), 33));
        System.out.printf ("  |  %-12s : %-33s|%n", "Priority",    next.getPriority());
        System.out.printf ("  |  %-12s : %-33s|%n", "Status",      next.getStatus());
        System.out.printf ("  |  %-12s : %-33s|%n", "Est. Hours",  next.getEstimatedHours() + " hrs");
        System.out.printf ("  |  %-12s : %-33s|%n", "Description", trunc(next.getDescription(), 33));
        System.out.println("  +--------------------------------------------------+");
        System.out.println();
    }

    // ── option 7 : complete task ──────────────────────────────────
    private void completeTask() {
        printSectionHeader("COMPLETE TASK");
        System.out.print("  Task ID to mark as complete : ");
        String taskId = scanner.nextLine().trim().toUpperCase();
        System.out.println();
        engine.completeTask(taskId);
        System.out.println();
    }

    // ── option 8 : undo ───────────────────────────────────────────
    private void undoLastAllocation() {
        printSectionHeader("UNDO LAST ALLOCATION");
        try {
            engine.undoLastAllocation();
        } catch (ResourceNotFoundException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
        System.out.println();
    }

    // ── option 9 : allocation history ─────────────────────────────
    private void viewAllocationHistory() {
        printSectionHeader("ALLOCATION HISTORY");
        Stack<Allocation> history = engine.getAllocationHistory();
        if (history.isEmpty()) {
            System.out.println("  No allocations made yet.\n");
            return;
        }
        printAllocationTableHeader();
        for (Allocation a : history) {
            printAllocationRow(a);
        }
        printAllocationTableFooter();
    }

    // ── option 10 : resource report ───────────────────────────────
    private void viewResourceReport() {
        printSectionHeader("RESOURCE DETAIL REPORT");
        System.out.print("  Enter Resource ID : ");
        String resourceId = scanner.nextLine().trim().toUpperCase();
        System.out.println();

        boolean found = false;
        for (Resource r : engine.getAllResources()) {
            if (r.getResourceId().equals(resourceId)) {
                System.out.println(r.generateReport());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("  [!] Resource not found: " + resourceId);
        }
        System.out.println();
    }

    // ── option 11 : search ────────────────────────────────────────
    private void searchResource() {
        printSectionHeader("SEARCH RESOURCE BY NAME");
        System.out.print("  Keyword : ");
        String keyword = scanner.nextLine().trim();
        System.out.println();

        List<Resource> results = engine.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("  No resources found matching \"" + keyword + "\".\n");
            return;
        }
        System.out.println("  Found " + results.size() + " result(s) for \"" + keyword + "\":\n");
        printResourceTableHeader();
        for (Resource r : results) {
            printResourceRow(r);
        }
        printResourceTableFooter();
    }

    // ── option 12 : full report ───────────────────────────────────
    private void generateFullReport() {
        System.out.println(engine.generateFullReport());
    }

    // ==========================================================
    //   TABLE PRINTERS
    // ==========================================================

    private void printResourceTableHeader() {
        String line = "  +" + rep('-', W_ID+2) + "+" + rep('-', W_NAME+2)
                    + "+" + rep('-', W_TYPE+2) + "+" + rep('-', W_SPEC+2)
                    + "+" + rep('-', W_STAT+2) + "+";
        System.out.println(line);
        System.out.printf("  | %-"+W_ID+"s | %-"+W_NAME+"s | %-"+W_TYPE+"s | %-"+W_SPEC+"s | %-"+W_STAT+"s |%n",
            "ID", "Name", "Type", "Specification", "Status");
        System.out.println(line);
    }

    private void printResourceRow(Resource r) {
        String status = r.isAvailable() ? "AVAILABLE" : "Alloc -> " + r.getAllocatedTaskId();
        System.out.printf("  | %-"+W_ID+"s | %-"+W_NAME+"s | %-"+W_TYPE+"s | %-"+W_SPEC+"s | %-"+W_STAT+"s |%n",
            r.getResourceId(),
            trunc(r.getName(),          W_NAME),
            r.getType(),
            trunc(r.getSpecification(), W_SPEC),
            trunc(status,               W_STAT));
    }

    private void printResourceTableFooter() {
        System.out.println("  +" + rep('-', W_ID+2) + "+" + rep('-', W_NAME+2)
            + "+" + rep('-', W_TYPE+2) + "+" + rep('-', W_SPEC+2)
            + "+" + rep('-', W_STAT+2) + "+");
        System.out.println();
    }

    private void printTaskTableHeader() {
        String line = "  +" + rep('-', T_ID+2) + "+" + rep('-', T_NAME+2)
                    + "+" + rep('-', T_PRI+2)  + "+" + rep('-', T_STAT+2)
                    + "+" + rep('-', T_RES+2)  + "+" + rep('-', T_COST+2) + "+";
        System.out.println(line);
        System.out.printf("  | %-"+T_ID+"s | %-"+T_NAME+"s | %-"+T_PRI+"s | %-"+T_STAT+"s | %-"+T_RES+"s | %-"+T_COST+"s |%n",
            "ID", "Task Name", "Priority", "Status", "Res#", "Est. Cost (Rs)");
        System.out.println(line);
    }

    private void printTaskRow(Task t) {
        System.out.printf("  | %-"+T_ID+"s | %-"+T_NAME+"s | %-"+T_PRI+"s | %-"+T_STAT+"s | %-"+T_RES+"d | %-"+T_COST+".0f |%n",
            t.getTaskId(),
            trunc(t.getTaskName(), T_NAME),
            t.getPriority(),
            t.getStatus(),
            t.getAllocatedResourceIds().size(),
            t.getEstimatedCost());
    }

    private void printTaskTableFooter() {
        System.out.println("  +" + rep('-', T_ID+2) + "+" + rep('-', T_NAME+2)
            + "+" + rep('-', T_PRI+2) + "+" + rep('-', T_STAT+2)
            + "+" + rep('-', T_RES+2) + "+" + rep('-', T_COST+2) + "+");
        System.out.println();
    }

    private void printAllocationTableHeader() {
        String line = "  +" + rep('-', A_ID+2) + "+" + rep('-', A_RES+2)
                    + "+" + rep('-', A_TASK+2) + "+" + rep('-', A_TIME+2)
                    + "+" + rep('-', A_STAT+2) + "+";
        System.out.println(line);
        System.out.printf("  | %-"+A_ID+"s | %-"+A_RES+"s | %-"+A_TASK+"s | %-"+A_TIME+"s | %-"+A_STAT+"s |%n",
            "Alloc ID", "Resource", "Task", "Allocated At", "Status");
        System.out.println(line);
    }

    private void printAllocationRow(Allocation a) {
        String raw     = a.toString();
        String allocId = extract(raw, "[", "]");
        String resource= extract(raw, "Resource: ", " -> Task:");
        String task    = extract(raw, "Task: ", " | Allocated:");
        String time    = extract(raw, "Allocated: ", " | Status:");
        String status  = extract(raw, "Status: ", null);

        System.out.printf("  | %-"+A_ID+"s | %-"+A_RES+"s | %-"+A_TASK+"s | %-"+A_TIME+"s | %-"+A_STAT+"s |%n",
            trunc(allocId,  A_ID),
            trunc(resource, A_RES),
            trunc(task,     A_TASK),
            trunc(time,     A_TIME),
            trunc(status,   A_STAT));
    }

    private void printAllocationTableFooter() {
        System.out.println("  +" + rep('-', A_ID+2) + "+" + rep('-', A_RES+2)
            + "+" + rep('-', A_TASK+2) + "+" + rep('-', A_TIME+2)
            + "+" + rep('-', A_STAT+2) + "+");
        System.out.println();
    }

    // ==========================================================
    //   HELPERS
    // ==========================================================

    private void printBanner(String title, String subtitle) {
        System.out.println();
        System.out.println("  +================================================+");
        System.out.printf ("  |  %-44s  |%n", title);
        System.out.printf ("  |  %-44s  |%n", subtitle);
        System.out.println("  +================================================+");
        System.out.println();
    }

    private void printSectionHeader(String title) {
        String bar = rep('-', 52);
        System.out.println("  +" + bar + "+");
        System.out.printf ("  |  %-50s  |%n", title);
        System.out.println("  +" + bar + "+");
    }

    private String rep(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    private String trunc(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 1) + "~";
    }

    private String extract(String src, String from, String to) {
        int start = src.indexOf(from);
        if (start < 0) return "";
        start += from.length();
        if (to == null) return src.substring(start).trim();
        int end = src.indexOf(to, start);
        if (end < 0) return src.substring(start).trim();
        return src.substring(start, end).trim();
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
