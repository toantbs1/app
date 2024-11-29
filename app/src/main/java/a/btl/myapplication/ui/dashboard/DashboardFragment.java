package a.btl.myapplication.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import a.btl.myapplication.R;
import a.btl.myapplication.databinding.FragmentDashboardBinding;
import a.btl.myapplication.entity.dto.BMIOnDay;
import a.btl.myapplication.entity.dto.ExerciseOnDay;
import a.btl.myapplication.utils.DateConverter;
import a.btl.myapplication.utils.DateValueFormatter;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private EditText txtWeight, txtHeight;
    private DashboardViewModel historyViewModel;
    private LineChart lineChart, lineChart1;
    private TextView tvBMI, tvStatus;
    private Button btnSave;
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        getWidget();
        btnSave.setOnClickListener(view -> {
            if (txtHeight.getText() != null && txtWeight.getText() != null) {
                double bmi = tinhBMI(Float.parseFloat(txtWeight.getText().toString()), Float.parseFloat(txtHeight.getText().toString()));
                tvBMI.setText(String.format("%.2f", bmi));
                tvStatus.setText(setStatus(bmi));
                historyViewModel.insertHistory(getContext(), bmi);
                getDataFromModel();
            }
        });
        getDataFromModel();
        return root;
    }

    private void getDataFromModel() {
        historyViewModel.getHistory(getContext()).observe(getViewLifecycleOwner(), histories -> {
            if (histories != null && !histories.isEmpty()) {
                updateChart(histories);
            }
        });
        historyViewModel.getCaloList(getContext()).observe(getViewLifecycleOwner(), this::updateCaloChart);
    }

    private void getWidget() {
        root = binding.getRoot();
        historyViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        txtWeight = root.findViewById(R.id.txt_weight);
        txtHeight = root.findViewById(R.id.txt_height);
        tvBMI = root.findViewById(R.id.tv_bmi);
        tvStatus = root.findViewById(R.id.tv_status);
        btnSave = root.findViewById(R.id.btn_save_bmi);
        lineChart = root.findViewById(R.id.lineChart);
        lineChart1 = root.findViewById(R.id.lineChart1);

    }

    private double tinhBMI(float w, float h) {
        return Double.parseDouble(String.valueOf(w / (h * h)));
    }

    private String setStatus(double bmi) {
        if (bmi < 18.5) {
            return "Thiếu cân";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Bình thường";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Thừa cân";
        } else {
            return "Béo phì";
        }
    }

    private void configChart(List<Entry> entries, String label, LineChart lineChart) {
        LineDataSet dataSet = new LineDataSet(entries, label);

        // Cấu hình cho LineDataSet
        dataSet.setColor(Color.BLUE); // Màu của đường
        dataSet.setValueTextColor(Color.BLACK); // Màu văn bản của giá trị
        dataSet.setValueTextSize(12f); // Kích thước văn bản của giá trị
        dataSet.setLineWidth(2f); // Độ dày của đường
        dataSet.setCircleColor(Color.RED); // Màu của các điểm tròn
        dataSet.setCircleRadius(4f); // Kích thước của các điểm tròn

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Cập nhật biểu đồ

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getXAxis().setValueFormatter(new DateValueFormatter());

        // Cấu hình biểu đồ
        lineChart.getXAxis().setLabelCount(7);
        lineChart.getDescription().setEnabled(false); // Tắt mô tả
        lineChart.getXAxis().setGranularity(1f); // Đặt khoảng cách giữa các nhãn trên trục x
        lineChart.getXAxis().setDrawGridLines(false); // Ẩn các đường lưới
        lineChart.getAxisRight().setEnabled(false); // Tắt trục y bên phải
        lineChart.getXAxis().setDrawLabels(true); // Hiển thị nhãn

        // Bật zoom khi nhấn đúp
        lineChart.setDragEnabled(true); // Bật kéo để xem
        lineChart.setScaleEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(true);

        // Cập nhật biểu đồ
        lineChart.invalidate();
    }

    private void updateChart(List<BMIOnDay> histories) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < histories.size(); i++) {
            float bmi = (float) histories.get(i).getBMI();
            long timestamp = DateConverter.dateToDateTime(histories.get(i).getDate() + " 00:00:00").getTime();
            entries.add(new Entry(timestamp, bmi)); // Sử dụng timestamp cho trục x
        }
        configChart(entries, "BMI Over Time", lineChart);
    }

    private void updateCaloChart(List<ExerciseOnDay> caloList) {
        // Tạo dữ liệu cho biểu đồ calo
        List<Entry> caloEntries = new ArrayList<>();
        for (int i = 0; i < caloList.size(); i++) {
            long timestamp = DateConverter.dateToDateTime(caloList.get(i).getDate() + " 00:00:00").getTime();
            caloEntries.add(new Entry(timestamp, caloList.get(i).getCalo()));
        }
        configChart(caloEntries, "Calories Consumed", lineChart1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}