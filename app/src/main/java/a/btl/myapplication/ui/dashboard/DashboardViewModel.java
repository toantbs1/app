package a.btl.myapplication.ui.dashboard;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import a.btl.myapplication.entity.History;
import a.btl.myapplication.entity.dto.BMIOnDay;
import a.btl.myapplication.entity.dto.ExerciseOnDay;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.utils.AppDatabase;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<List<BMIOnDay>> historyList;
    private MutableLiveData<List<ExerciseOnDay>> caloList;
    private AppDatabase db;

    public DashboardViewModel() {
        historyList = new MutableLiveData<>();
        caloList = new MutableLiveData<>();
    }

    public LiveData<List<BMIOnDay>> getHistory(Context context) {
        loadHistory(context);
        return historyList;
    }

    public LiveData<List<ExerciseOnDay>> getCaloList(Context context) {
        loadCalo(context);
        return caloList;
    }

    private void loadCalo(Context context) {
        db = AppDatabase.getInstance(context);
        new Thread(() -> {
            List<ExerciseOnDay> caloListData = new ArrayList<>();
            List<String> dates = db.historyDao().getDistinctDates();
            for (String date : dates) {
                ExerciseOnDay exerciseOnDay = new ExerciseOnDay();
                List<Integer> e = db.historyDao().getExerciseIdByNgayTaoAndUserIdAndBMI(date, UserSession.getInstance(context).getUserId(), Double.valueOf(0));
                Integer calo = 0;
                //Integer calo = db.exercisesDao().getTotalCaloByDate(date, UserSession.getInstance().getUserId());
                for (Integer i : e) {
                    calo += db.exercisesDao().getCaloByExerciseId(i);
                }
                exerciseOnDay.setDate(date);
                exerciseOnDay.setCalo(calo);
                caloListData.add(exerciseOnDay);
            }
            caloList.postValue(caloListData);
        }).start();
    }

    private void loadHistory(Context context) {
        db = AppDatabase.getInstance(context);
        new Thread(() -> {
            List<BMIOnDay> caloListData = new ArrayList<>();
            List<String> dates = db.historyDao().getDistinctDates();
            for (String date : dates) {
                BMIOnDay exerciseOnDay = new BMIOnDay();
                History e = db.historyDao().getHistoryByNgayTaoAndUserIdAndBMI(date, UserSession.getInstance(context).getUserId(), Double.valueOf(0));
                exerciseOnDay.setDate(date);
                exerciseOnDay.setBMI(e.getBMI());
                caloListData.add(exerciseOnDay);
            }
            historyList.postValue(caloListData);
        }).start();
    }

    public void insertHistory(Context context, double bmi) {
        db = AppDatabase.getInstance(context);
        new Thread(() -> {
            History history = new History();
            history.setUserId(UserSession.getInstance(context).getUserId());
            history.setExerciseId(1);
            history.setBMI(bmi);
            history.setNgayTao(new Date());
            history.setIsFav1(false);
            db.historyDao().insert(history);
        }).start();
    }
}