package DataLayer;

import DTO.EstimateWeeklyWeightDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcEstimateWeeklyWeightDao implements EstimateWeeklyWeightDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEstimateWeeklyWeightDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EstimateWeeklyWeightDto> rowMapper = new RowMapper<>() {
        @Override
        public EstimateWeeklyWeightDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            EstimateWeeklyWeightDto dto = new EstimateWeeklyWeightDto();
            dto.setBranchId(rs.getInt("branch_id"));
            dto.setDay(rs.getString("day"));
            dto.setTotalWeight(rs.getDouble("total_weight"));
            return dto;
        }
    };

    @Override
    public List<EstimateWeeklyWeightDto> findAll() {
        String sql = "SELECT * FROM deliveries.estimate_weekly_weight";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public EstimateWeeklyWeightDto findByBranchAndDay(int branchId, String day) {
        String sql = "SELECT * FROM deliveries.estimate_weekly_weight WHERE branch_id = ? AND day = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, branchId, day);
    }

    @Override
    public void save(EstimateWeeklyWeightDto dto) {
        String sql = "INSERT INTO deliveries.estimate_weekly_weight (branch_id, day, total_weight) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, dto.getBranchId(), dto.getDay(), dto.getTotalWeight());
    }

    @Override
    public void update(EstimateWeeklyWeightDto dto) {
        String sql = "UPDATE deliveries.estimate_weekly_weight SET total_weight = ? WHERE branch_id = ? AND day = ?";
        jdbcTemplate.update(sql, dto.getTotalWeight(), dto.getBranchId(), dto.getDay());
    }

    @Override
    public void delete(EstimateWeeklyWeightDto dto) {
        String sql = "DELETE FROM deliveries.estimate_weekly_weight WHERE branch_id = ? AND day = ?";
        jdbcTemplate.update(sql, dto.getBranchId(), dto.getDay());
    }
}
