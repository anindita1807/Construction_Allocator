package src.models;

public class EquipmentResource extends Resource {
    private String equipmentType;
    private int capacityTons;
    private double rentalCostPerHour;
    private boolean requiresOperator;

    public EquipmentResource(String resourceId, String name,
                             String equipmentType, int capacityTons,
                             double rentalCostPerHour, boolean requiresOperator) {
        super(resourceId, name, ResourceType.EQUIPMENT);
        this.equipmentType = equipmentType;
        this.capacityTons = capacityTons;
        this.rentalCostPerHour = rentalCostPerHour;
        this.requiresOperator = requiresOperator;
    }

    @Override
    public double getCostPerHour() {
        return rentalCostPerHour;
    }
    @Override
    public String getSpecification() {
        return String.format("Type: %s | Capacity: %d tons | Needs Operator: %s",
            equipmentType, capacityTons, requiresOperator ? "Yes" : "No");
    }

    @Override
    public String generateReport() {
        return String.format(
            "EQUIPMENT RESOURCE REPORT\n" +
            "-------------------------\n" +
            "ID         : %s\n" +
            "Name       : %s\n" +
            "Type       : %s\n" +
            "Capacity   : %d tons\n" +
            "Cost/Hr    : Rs %.0f\n" +
            "Operator   : %s\n" +
            "Total Allocations: %d\n" +
            "Status     : %s",
            getResourceId(), getName(), equipmentType, capacityTons,
            rentalCostPerHour, requiresOperator ? "Required" : "Not Required",
            getTotalAllocations(),
            isAvailable() ? "Available" : "Busy on Task " + getAllocatedTaskId()
        );
    }

    public String getEquipmentType() { return equipmentType; }
    public boolean isRequiresOperator() { return requiresOperator; }
}

