package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "exercises", foreignKeys = {@ForeignKey(entity = ListExercises.class, parentColumns = "listExerciseId", childColumns = "listExerciseId")})
public class Exercises implements Serializable {

    @PrimaryKey
    private Integer exerciseId;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "video")
    private String video;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "calo")
    private Integer calo;

    @ColumnInfo(name = "listExerciseId")
    private Integer listExerciseId;

    @ColumnInfo(name = "timeOrNum")
    private String timeOrNum;

    @ColumnInfo(name = "avatar")
    private String avatar;

    public Exercises() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimeOrNum() {
        return timeOrNum;
    }

    public void setTimeOrNum(String timeOrNum) {
        this.timeOrNum = timeOrNum;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getListExerciseId() {
        return listExerciseId;
    }

    public void setListExerciseId(Integer listExerciseId) {
        this.listExerciseId = listExerciseId;
    }

    public Integer getCalo() {
        return calo;
    }

    public void setCalo(Integer calo) {
        this.calo = calo;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Kiểm tra tham chiếu
        if (obj == null || getClass() != obj.getClass()) return false; // Kiểm tra loại
        Exercises exercise = (Exercises) obj; // Ép kiểu
        return Objects.equals(exerciseId, exercise.exerciseId); // So sánh theo exerciseId
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId); // Tạo mã hash dựa trên exerciseId
    }
}
