package src.models;

public class MaterialResource extends Resource {
    private String unit;
    private double quantityAvailable;
    private double quantityAllocated;
    private double costPerUnit;

    public MaterialResource(String resourceId, String name,
                            String unit, double quantityAvailable, double costPerUnit) {
        super(resourceId, name, ResourceType.MATERIAL);
        this.unit = unit;
        this.quantityAvailable = quantityAvailable;
        this.quantityAllocated = 0;
        this.costPerUnit = costPerUnit;
    }

    // Materials work differently — partial allocation possible
    public boolean allocateMaterial(String taskId, double quantity) {
        if (quantity <= quantityAvailable) {
            quantityAvailable -= quantity;
            quantityAllocated += quantity;
            if (quantityAvailable == 0) allocate(taskId);
            return true;
        }
        return false;
    }

    @Override
    public double getCostPerHour() {
        return costPerUnit;
    }

    @Override
    public String getSpecification() {
        return String.format("Unit: %s | Available: %.1f | Cost: Rs %.0f per %s",
            unit, quantityAvailable, costPerUnit, unit);
    }

    @Override
    public String generateReport() {
        return String.format(
            "MATERIAL RESOURCE REPORT\n" +
            "------------------------\n" +
            "ID          : %s\n" +
            "Name        : %s\n" +
            "Unit        : %s\n" +
            "Available   : %.1f %s\n" +
            "Allocated   : %.1f %s\n" +
            "Cost/Unit   : Rs %.0f\n" +
            "Status      : %s",
            getResourceId(), getName(), unit,
            quantityAvailable, unit, quantityAllocated, unit,
            costPerUnit,
            quantityAvailable > 0 ? "In Stock" : "Out of Stock"
        );
    }

    public double getQuantityAvailable() { return quantityAvailable; }
    public String getUnit() { return unit; }
}

