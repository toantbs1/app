package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import a.btl.myapplication.entity.Type;

@Dao
public interface TypeDao {
    @Query("SELECT * FROM type")
    List<Type> getAllTypes();
}
