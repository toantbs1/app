package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import a.btl.myapplication.entity.History;

@Dao
public interface HistoryDao {
    @Insert
    void insert(History history);

    @Query("SELECT * FROM history WHERE DATE(ngayTao) = :ngayTao AND userId = :userId AND BMI != :bmi ORDER BY ngayTao DESC LIMIT 1")
    History getHistoryByNgayTaoAndUserIdAndBMI(String ngayTao, Integer userId, Double bmi);

    @Query("SELECT DISTINCT DATE(ngayTao) AS date_only FROM history")
    List<String> getDistinctDates();

    @Query("SELECT exerciseId FROM history WHERE DATE(ngayTao) = :ngayTao AND userId = :userId AND BMI != :bmi")
    List<Integer> getExerciseIdByNgayTaoAndUserIdAndBMI(String ngayTao, Integer userId, Double bmi);
}
