package DataLayer;

import DTO.EstimateWeeklyWeightDto;

import java.util.List;

public interface EstimateWeeklyWeightDao {
    List<EstimateWeeklyWeightDto> findAll();
    EstimateWeeklyWeightDto findByBranchAndDay(int branchId,String day);
    void save(EstimateWeeklyWeightDto dto);
    void update(EstimateWeeklyWeightDto dto);
    void delete(EstimateWeeklyWeightDto dto);
}
