package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "time_notifications", foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId")})
public class TimeNotification implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer timeId;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "daysOfWeek")
    private String daysOfWeek = "0,0,0,0,0,0,0";

    @ColumnInfo(name = "enabled")
    private Integer enabled;

    @ColumnInfo(name = "userId")
    private Integer userId;

    public TimeNotification() {
    }

    // Getter và Setter
    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer id) {
        this.timeId = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public void setIsEnabled(boolean enabled) {
        if (enabled) {
            this.enabled = 1;
        } else {
            this.enabled = 0;
        }
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public Boolean getIsEnabled() {
        if (enabled == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

