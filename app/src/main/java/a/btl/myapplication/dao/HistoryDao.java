package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import a.btl.myapplication.entity.History;

@Dao
public interface HistoryDao {
    @Insert
    void insert(History history);

    @Update
    void update(History history);

    @Query(("SELECT COUNT(*) FROM history WHERE isFav = :isFav AND exerciseId = :exerciseId AND userId = :userId"))
    int countByIsFavAndExerciseIdAndUserId(Integer isFav, Integer exerciseId, Integer userId);

    @Query("SELECT * FROM history WHERE isFav = :isFav AND exerciseId = :exerciseId AND userId = :userId")
    History getHistoryByIsFavAndExerciseIdAndUserId(Integer isFav, Integer exerciseId, Integer userId);

    @Query("SELECT * FROM history WHERE DATE(ngayTao) = :ngayTao AND userId = :userId AND BMI != :bmi ORDER BY ngayTao DESC LIMIT 1")
    History getHistoryByNgayTaoAndUserIdAndBMI(String ngayTao, Integer userId, Double bmi);

    @Query("SELECT DISTINCT DATE(ngayTao) AS date_only FROM history")
    List<String> getDistinctDates();

    @Query("SELECT exerciseId FROM history WHERE DATE(ngayTao) = :ngayTao AND userId = :userId AND BMI = :bmi")
    List<Integer> getExerciseIdByNgayTaoAndUserIdAndBMI(String ngayTao, Integer userId, Double bmi);
}
