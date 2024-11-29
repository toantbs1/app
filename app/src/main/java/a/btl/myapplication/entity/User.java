package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user", indices = {
        @Index(value = "email", unique = true),
        @Index(value = "username", unique = true)
}, foreignKeys = {
        @ForeignKey(entity = Type.class, parentColumns = "typeId", childColumns = "typeId")
})
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int userId;

    @ColumnInfo(name = "hoten")
    @NonNull
    private String hoten;

    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    @ColumnInfo(name = "username")
    @NonNull
    private String username;

    @ColumnInfo(name = "password")
    @NonNull
    private String password;

    @ColumnInfo(name = "typeId")
    @NonNull
    private Integer typeId;

    public User() {
    }

    public User(@NonNull String email, @NonNull String hoten, @NonNull String username, @NonNull String password, @NonNull Integer typeId) {
        this.email = email;
        this.hoten = hoten;
        this.username = username;
        this.password = password;
        this.typeId = typeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    public String getHoten() {
        return hoten;
    }

    public void setHoten(@NonNull String hoten) {
        this.hoten = hoten;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(@NonNull Integer typeId) {
        this.typeId = typeId;
    }
}
