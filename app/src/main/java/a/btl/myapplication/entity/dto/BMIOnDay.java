package a.btl.myapplication.entity.dto;

public class BMIOnDay {
    private String date;
    private double BMI;

    public BMIOnDay() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }
}
