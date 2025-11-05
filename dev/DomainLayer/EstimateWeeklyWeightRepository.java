package DomainLayer;

import DTO.EstimateWeeklyWeightDto;
import DataLayer.EstimateWeeklyWeightDao;
import DataLayer.JdbcEstimateWeeklyWeightDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import DataLayer.DatabaseConnector;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public class EstimateWeeklyWeightRepository {
    private final EstimateWeeklyWeightDao estimateDao;
    private static EstimateWeeklyWeightRepository instance = null;
    private EstimateWeeklyWeightRepository(EstimateWeeklyWeightDao dao){
        this.estimateDao = dao;
    }
    public static EstimateWeeklyWeightRepository getInstance() {
        if (instance == null) {
            instance = new EstimateWeeklyWeightRepository(
                    new JdbcEstimateWeeklyWeightDao(
                            new JdbcTemplate(DatabaseConnector.getDataSource())
                    )
            );
        }
        return instance;
    }

    public EstimateWeeklyWeightDto findByBranchIdAndDay(int branchId, DayOfWeek day) {
        return estimateDao.findByBranchAndDay(branchId, day.toString());
    }

    public void update(EstimateWeeklyWeightDto dto) {
        estimateDao.update(dto);

    }
    public void save(EstimateWeeklyWeightDto dto) {
        estimateDao.save(dto);
    }

    public void saveInitialDaysForNewBranch(int branchId) {
        for (DayOfWeek day : DayOfWeek.values()) {
            EstimateWeeklyWeightDto dto = new EstimateWeeklyWeightDto(branchId, day.toString(), 0);
            estimateDao.save(dto);
        }
    }
    public void delete(int branchId, DayOfWeek day) {
        EstimateWeeklyWeightDto dto = estimateDao.findByBranchAndDay(branchId, day.toString());
        if (dto != null) {
            estimateDao.delete(dto);
        }
    }

    public List<EstimateWeeklyWeightDto> findAll() {
        return estimateDao.findAll();
    }

}
