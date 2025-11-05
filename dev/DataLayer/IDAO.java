package DataLayer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDAO<T> {
    ArrayList<T> getAll() throws SQLException;
    T getById(String id) throws SQLException;
    void addT(T object) throws SQLException;
    void updateT(T object) throws SQLException;
    void deleteT(String id) throws SQLException;
} 