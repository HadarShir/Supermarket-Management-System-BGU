package DomainLayer;

import DTO.EstimateWeeklyWeightDto;
import DTO.TruckDTO;
import DTO.TruckScheduleDTO;
import DataLayer.JdbcTruckScheduleDAO;
import DataLayer.TruckDAO;
import DataLayer.JdbcTruckDAO;
import DataLayer.TruckScheduleDAO;
import DomainLayer.Truck;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;


public class TruckRepository {
    private final TruckDAO truckDAO;
    private final TruckScheduleDAO truckScheduleDAO;
    private final ArrayList<Truck> trucks;
    private final ArrayList<TruckSchedule> schedules;

    public TruckRepository() {
        this.truckDAO = new JdbcTruckDAO();
        this.truckScheduleDAO = new JdbcTruckScheduleDAO();
        this.trucks = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public Truck getTruckByLicensePlate(String licensePlate) throws SQLException {
        for (Truck t : trucks) {
            if (t.getLicensePlate().equals(licensePlate)) {
                return t;
            }
        }
        TruckDTO dto = truckDAO.findByLicensePlate(licensePlate);
        System.out.println("[DEBUG] DTO: " + dto);
        if (dto == null) return null;

        Truck truck = Truck.fromDTO(dto);
        System.out.println("[DEBUG] built Truck: " + truck);

        trucks.add(truck);
        return truck;
    }

    public ArrayList<Truck> getAllTrucks() throws SQLException {
        trucks.clear();
        ArrayList<TruckDTO> dtos = truckDAO.findAll();
        for (TruckDTO dto : dtos) {
            Truck truck = Truck.fromDTO(dto);
            trucks.add(truck);
        }
        return trucks;
    }

    public void addTruck(TruckDTO truckDTO) throws SQLException {
        truckDAO.save(truckDTO);
        Truck truck = Truck.fromDTO(truckDTO);
        trucks.add(truck);
    }

    public void deleteTruck(String licensePlate) throws SQLException {
        truckDAO.delete(licensePlate);
        trucks.removeIf(t -> t.getLicensePlate().equals(licensePlate));
    }

    public void addSchedule(TruckScheduleDTO scheduleDTO) {
        TruckSchedule truckSchedule = TruckSchedule.fromDTO(scheduleDTO);
        truckScheduleDAO.save(truckSchedule);
        schedules.add(truckSchedule);
    }

    public TruckSchedule getSchedule(String licensePlate, int shiftNumber) {
        for (TruckSchedule s : schedules) {
            if (s.getLicensePlate().equals(licensePlate) && s.getShiftNumber() == shiftNumber) {
                return s;
            }
        }
        TruckSchedule fetched = truckScheduleDAO.find(licensePlate, shiftNumber);
        if (fetched != null) schedules.add(fetched);
        return fetched;
    }

    public void updateSchedule(TruckSchedule schedule) {
        truckScheduleDAO.update(schedule);
        for (int i = 0; i < schedules.size(); i++) {
            TruckSchedule s = schedules.get(i);
            if (s.getLicensePlate().equals(schedule.getLicensePlate()) && s.getShiftNumber() == schedule.getShiftNumber()) {
                schedules.set(i, schedule);
                break;
            }
        }
    }

    public void deleteSchedule(String licensePlate, int shiftNumber) {
        truckScheduleDAO.delete(licensePlate, shiftNumber);
        schedules.removeIf(s -> s.getLicensePlate().equals(licensePlate) && s.getShiftNumber() == shiftNumber);
    }

    public void clearSchedules() {
        schedules.clear();
        this.truckScheduleDAO.clearAllSchedules();
    }

    public List<TruckSchedule> getAllSchedules() {
        return new ArrayList<>(schedules);
    }


}


