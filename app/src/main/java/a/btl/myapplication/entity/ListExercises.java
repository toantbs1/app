package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "list_exercises", foreignKeys = {@ForeignKey(entity = Type.class, parentColumns = "typeId", childColumns = "typeId")})
public class ListExercises implements Serializable {

    @PrimaryKey
    private Integer listExerciseId;

    @ColumnInfo(name = "listExerciseName")
    @NonNull
    private String listExerciseName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "typeId")
    private Integer typeId;

    public ListExercises() {
    }

    public Integer getListExerciseId() {
        return listExerciseId;
    }

    public void setListExerciseId(Integer listExerciseId) {
        this.listExerciseId = listExerciseId;
    }

    @NonNull
    public String getListExerciseName() {
        return listExerciseName;
    }

    public void setListExerciseName(@NonNull String listExerciseName) {
        this.listExerciseName = listExerciseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
