package DataLayer;

import DTO.DriverAssignmentDTO;
import java.util.List;

public interface DriverAssignmentDAO {
    List<String> getUnassignedDrivers(int branchId, int shiftNum) throws Exception;
    void insertDriverAssignments(List<DriverAssignmentDTO> assignments) throws Exception;
    void markDriverAsAssigned(int branchId, int shiftNum, String driverName) throws Exception;
    boolean hasAssignments(int branchId, int shiftNum) throws Exception;
}
