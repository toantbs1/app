package a.btl.myapplication.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateValueFormatter extends ValueFormatter {
    private final SimpleDateFormat dateFormat;

    public DateValueFormatter() {
        // Định dạng ngày theo nhu cầu
        this.dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // Chuyển đổi giá trị số thành ngày tháng
        return dateFormat.format(new Date((long) value)); // Chuyển đổi giá trị float thành Date
    }
}