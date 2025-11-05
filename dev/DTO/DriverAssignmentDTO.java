package DTO;

public class DriverAssignmentDTO {
    private int branchId;
    private int shiftNum;
    private String driverName;
    private boolean assigned;

    public DriverAssignmentDTO(int branchId, int shiftNum, String driverName, boolean assigned) {
        this.branchId = branchId;
        this.shiftNum = shiftNum;
        this.driverName = driverName;
        this.assigned = assigned;
    }

    public int getBranchId() { return branchId; }
    public int getShiftNum() { return shiftNum; }
    public String getDriverName() { return driverName; }
    public boolean isAssigned() { return assigned; }

    public void setAssigned(boolean assigned) { this.assigned = assigned; }
}
