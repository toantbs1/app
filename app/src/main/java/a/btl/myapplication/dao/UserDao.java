package a.btl.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Optional;

import a.btl.myapplication.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    Optional<User> getUserByUsername(String username);

    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    int countUserByUsername(String username);

    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    int countUserByEmail(String email);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User findByEmail(String email);
}
