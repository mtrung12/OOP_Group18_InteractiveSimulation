package model.force;

public class Force {
    private double value;

    public Force(double value){
        this.value = value;
    }


    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
