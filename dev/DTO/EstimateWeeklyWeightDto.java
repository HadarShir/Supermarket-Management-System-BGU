package DTO;

public class EstimateWeeklyWeightDto {
    private int branchId;
    private String day;
    private double totalWeight;

    public EstimateWeeklyWeightDto(){}
    public EstimateWeeklyWeightDto(int branchId, String day, double totalWeight) {
        this.branchId = branchId;
        this.day = day;
        this.totalWeight = totalWeight;
    }

    public int getBranchId() { return branchId; }
    public String getDay() { return day; }
    public double getTotalWeight() { return totalWeight; }

    public void setBranchId(int branchId) { this.branchId = branchId; }
    public void setDay(String day) { this.day = day; }
    public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
}
