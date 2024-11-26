package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import a.btl.myapplication.entity.Exercises;

@Dao
public interface ExercisesDao {
    @Query("SELECT * FROM exercises WHERE listExerciseId = :id")
    List<Exercises> getAllExercisesByListExerciseId(Integer id);

    @Query("SELECT calo FROM exercises WHERE exerciseId = :exerciseId")
    Integer getCaloByExerciseId(Integer exerciseId);

    @Query("SELECT SUM(calo) FROM exercises WHERE exerciseId IN (SELECT exerciseId FROM history WHERE DATE(ngayTao) = :ngayTao AND userId = :userId)")
    Integer getTotalCaloByDate(String ngayTao, Integer userId);
}