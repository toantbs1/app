package a.btl.myapplication.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import a.btl.myapplication.entity.ListExercises;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.utils.AppDatabase;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<ListExercises>> listExercises;
    private AppDatabase db;

    public HomeViewModel() {
        listExercises = new MutableLiveData<>();
    }

    public LiveData<List<ListExercises>> getItems(Context context) {
        if (listExercises.getValue() == null) {
            loadItems(context);
        }
        return listExercises;
    }

    private void loadItems(Context context) {
        //khởi tạo và lấy một thể hiện của cơ sở dữ liệu sử dụng Room
        db = AppDatabase.getInstance(context);
        //Tạo luồng để thực hiện tác vụ bất đồng bộ
        new Thread(() -> {
            //Lấy typeId của lớp quản lý thông tin phiên làm việc của người dùng
            Integer typeId = UserSession.getInstance(context).getTypeId();
            //Truy xuất dữ liệu từ database
            List<ListExercises> exercises = db.listExercisesDao().getAllListExercisesByTypeId(typeId);
            listExercises.postValue(exercises);
        }).start();
    }
}