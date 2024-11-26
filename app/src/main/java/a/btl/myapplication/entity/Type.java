package a.btl.myapplication.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "type")
public class Type implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int typeId;

    @ColumnInfo(name = "typeName")
    @NonNull
    private String typeName;

    @ColumnInfo(name = "typeAvatar")
    private String typeAvatar;

    public String getTypeAvatar() {
        return typeAvatar;
    }

    public void setTypeAvatar(String typeAvatar) {
        this.typeAvatar = typeAvatar;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @NonNull
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(@NonNull String typeName) {
        this.typeName = typeName;
    }
}
