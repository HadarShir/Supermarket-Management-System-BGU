package DataLayer;

import DomainLayer.TruckSchedule;
import java.util.List;

public interface TruckScheduleDAO {
    void save(TruckSchedule schedule);
    TruckSchedule find(String licensePlate, int shiftNumber);
    List<TruckSchedule> findAll();
    void update(TruckSchedule schedule);
    void delete(String licensePlate, int shiftNumber);
    void clearAllSchedules();
}
