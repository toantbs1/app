package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import a.btl.myapplication.utils.DateConverter;

@Entity(tableName = "history", foreignKeys = {@ForeignKey(entity = Exercises.class, parentColumns = "exerciseId", childColumns = "exerciseId"), @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId")})
public class History implements Serializable {

    @PrimaryKey
    private Integer historyId;

    @ColumnInfo(name = "ngayTao")
    private Date ngayTao;

    @ColumnInfo(name = "BMI")
    private double BMI;

    @ColumnInfo(name = "userId")
    private Integer userId;

    @ColumnInfo(name = "exerciseId")
    private Integer exerciseId;

    public History() {
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    @NonNull
    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return DateConverter.dateTimeToDate(date);
    }

    @TypeConverter
    public static Date stringToDate(String dateString) {
        return DateConverter.dateToDateTime(dateString);
    }
}
