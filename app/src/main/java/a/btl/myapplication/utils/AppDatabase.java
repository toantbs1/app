package a.btl.myapplication.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import a.btl.myapplication.dao.ExercisesDao;
import a.btl.myapplication.dao.HistoryDao;
import a.btl.myapplication.dao.ListExercisesDao;
import a.btl.myapplication.dao.TimeNotificationDao;
import a.btl.myapplication.dao.TypeDao;
import a.btl.myapplication.dao.UserDao;
import a.btl.myapplication.entity.Exercises;
import a.btl.myapplication.entity.History;
import a.btl.myapplication.entity.ListExercises;
import a.btl.myapplication.entity.TimeNotification;
import a.btl.myapplication.entity.Type;
import a.btl.myapplication.entity.User;

@Database(entities = {User.class, Type.class, TimeNotification.class, ListExercises.class, Exercises.class, History.class}, version = 3)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract TimeNotificationDao timeNotificationDao();

    public abstract TypeDao typeDao();

    public abstract ListExercisesDao listExercisesDao();

    public abstract ExercisesDao exercisesDao();

    public abstract HistoryDao historyDao();

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitness.db")
                    .createFromAsset("fitness.db")
                    //.fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }
}