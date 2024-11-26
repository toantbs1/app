package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import a.btl.myapplication.entity.TimeNotification;

@Dao
public interface TimeNotificationDao {
    @Insert
    void insert(TimeNotification notification);

    @Query("SELECT * FROM time_notifications")
    List<TimeNotification> getAllNotifications();

    @Update
    void update(TimeNotification timeNotification);

    @Query("DELETE FROM time_notifications WHERE timeId = :id")
    void deleteById(int id);
}