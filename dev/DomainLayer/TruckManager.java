package DomainLayer;

import DTO.EstimateWeeklyWeightDto;
import DTO.TruckDTO;
import DTO.TruckScheduleDTO;
import DataLayer.EstimateWeeklyWeightDao;
import DataLayer.TruckDAO;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TruckManager {

    private final TruckRepository truckRepository = new TruckRepository();
    private static TruckManager instance = null;
    private EstimateWeeklyWeightRepository estimateWeeklyWeightRepository;

    private TruckManager() {}

    public static TruckManager getInstance() {
        if (instance == null) {
            instance = new TruckManager();
        }
        return instance;
    }

    public Status createTruck(String licensePlate, String licenseType, double weight,
                              double maxWeight, boolean b, String model) throws SQLException {
        if (truckRepository.getTruckByLicensePlate(licensePlate) != null)
        {
            return Status.truckAlreadyExists;
        }
        TruckDTO truckDTO = new TruckDTO(licensePlate,licenseType,weight,maxWeight,model);
        truckRepository.addTruck(truckDTO);
        return Status.success;
    }

    public Status removeTruck(String licensePlate) throws SQLException {
        if (truckRepository.getTruckByLicensePlate(licensePlate) == null)
        {
        return Status.truckNotFound;
        }
        truckRepository.deleteTruck(licensePlate);
        return Status.success;
    }

    // todo- maybe should return dto
    public ArrayList<Truck> getAllTrucks() throws SQLException {
        return truckRepository.getAllTrucks();
    }

    public Truck findTruckByLicensePlate(String licensePlate) throws SQLException {
        return truckRepository.getTruckByLicensePlate(licensePlate);
    }
    public String showAllTrucks() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Truck> list = truckRepository.getAllTrucks();
        for (Truck truck : list)
        {
            stringBuilder.append(truck.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    public TruckSchedule getTruckSchedule(String licensePlate, int shiftNumber) {
        return truckRepository.getSchedule(licensePlate, shiftNumber);
    }
    private List<Truck> getAllTrucksSortedByWeight() throws SQLException {
        List<Truck> trucks = truckRepository.getAllTrucks(); // נניח שיש לך פונקציה כזו
        trucks.sort(Comparator.comparingDouble(Truck::getMaxWeight).reversed());
        return trucks;
    }
    public Status canSendShifts() throws SQLException {
        List<EstimateWeeklyWeightDto> estimates = estimateWeeklyWeightRepository.findAll();
        if (estimates == null || estimates.isEmpty()) {
            return Status.noEstimates;
        }
        return Status.success;
    }

    public Status sendShifts() throws SQLException {
        List<EstimateWeeklyWeightDto> estimates = estimateWeeklyWeightRepository.findAll();

        for (EstimateWeeklyWeightDto dto : estimates) {
            double remainingWeight = dto.getTotalWeight();
            int branchId = dto.getBranchId();
            DayOfWeek day = DayOfWeek.valueOf(dto.getDay()); // נניח שהשדה הוא מחרוזת של שם היום
            int morningShift = getShiftIndex(day, false);
            int eveningShift = getShiftIndex(day, true);

            List<Truck> trucks = getAllTrucksSortedByWeight();

            // בוקר
            remainingWeight = assignDriversByWeight(remainingWeight, branchId, morningShift, trucks);

            // אם נשאר משקל - ערב
            if (remainingWeight > 0) {
                assignDriversByWeight(remainingWeight, branchId, eveningShift, trucks);
            }
        }
        return Status.success;
    }
    private double assignDriversByWeight(double weight, int branchId, int shiftId, List<Truck> trucks) {
        for (Truck truck : trucks) {
            if (weight <= 0) break;

            double capacity = truck.getMaxWeight();
            weight -= capacity;

            LicenseType license = truck.getRequiredLicense(); // מחזיר "C" או "C1"
            String role = "Driver_" + license;

            ShiftController.getInstance().changeRequirement(String.valueOf(branchId), String.valueOf(shiftId), role, "1");
        }
        return weight;
    }
    private int getShiftIndex(DayOfWeek day, boolean isEvening) {
        int base = switch (day) {
            case SUNDAY -> 0;
            case MONDAY -> 2;
            case TUESDAY -> 4;
            case WEDNESDAY -> 6;
            case THURSDAY -> 8;
            case FRIDAY -> 10;
            case SATURDAY -> 12;
        };
        return isEvening ? base+1:base;
    }

    public void updateTruckSchedule(TruckSchedule schedule) {
        truckRepository.updateSchedule(schedule);
    }

    public void clearTruckSchedulesForNewWeek() {
        truckRepository.clearSchedules();
    }

    public void addTruckSchedule(TruckScheduleDTO schedule) {
        truckRepository.addSchedule(schedule);
    }

    public void restartTruckSchedule(){
        truckRepository.clearSchedules();
    }


}
