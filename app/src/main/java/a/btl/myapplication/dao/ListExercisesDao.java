package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import a.btl.myapplication.entity.ListExercises;

@Dao
public interface ListExercisesDao {
    @Query("SELECT * FROM list_exercises WHERE typeId= :typeId")
    List<ListExercises> getAllListExercisesByTypeId(Integer typeId);

    @Query("SELECT * FROM list_exercises")
    List<ListExercises> getAllListExercises();
}
