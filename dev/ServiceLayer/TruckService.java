package ServiceLayer;

import DomainLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public class TruckService {
    private final TruckManager truckManager;

    public TruckService() {
        this.truckManager = TruckManager.getInstance();
    }

    public void restartSystem() throws SQLException {
        truckManager.restartTruckSchedule();
    }

    public String showAllTrucks() throws SQLException {
        return truckManager.showAllTrucks();
    }
    public Status createTruck(String licensePlate, String licenseType, double weight, double maxWeight,
                              boolean isAvailable, String model) throws SQLException {
        LicenseType requiredLicense;
         try {
             requiredLicense = LicenseType.valueOf(licenseType.toUpperCase());
         } catch (IllegalArgumentException e) {
             return Status.invalidInput;
         }
         return TruckManager.getInstance().createTruck(licensePlate, licenseType, weight, maxWeight,
                 true, model);
    }
    public Status sendShifts() throws SQLException {
        Status status = truckManager.canSendShifts();
        if (status != Status.success) {
            return status;
        }
        return truckManager.sendShifts();
    }
}