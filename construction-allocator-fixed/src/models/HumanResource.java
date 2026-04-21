package src.models;

public class HumanResource extends Resource {
    private String role;
    private int experienceYears;
    private double dailyWage;

    public HumanResource(String resourceId, String name,
                         String role, int experienceYears, double dailyWage) {
        super(resourceId, name, ResourceType.HUMAN);
        this.role = role;
        this.experienceYears = experienceYears;
        this.dailyWage = dailyWage;
    }

    @Override
    public double getCostPerHour() {
        return dailyWage / 8.0;
    }
    @Override
    public String getSpecification() {
        return String.format("Role: %s | Experience: %d years | Daily Wage: Rs %.0f",
            role, experienceYears, dailyWage);
    }

    @Override
    public String generateReport() {
        return String.format(
            "HUMAN RESOURCE REPORT\n" +
            "---------------------\n" +
            "ID       : %s\n" +
            "Name     : %s\n" +
            "Role     : %s\n" +
            "Exp      : %d years\n" +
            "Wage     : Rs %.0f/day\n" +
            "Cost/Hr  : Rs %.0f\n" +
            "Total Allocations: %d\n" +
            "Status   : %s",
            getResourceId(), getName(), role, experienceYears,
            dailyWage, getCostPerHour(), getTotalAllocations(),
            isAvailable() ? "Available" : "Busy on Task " + getAllocatedTaskId()
        );
    }

    public String getRole() { return role; }
    public int getExperienceYears() { return experienceYears; }
}
