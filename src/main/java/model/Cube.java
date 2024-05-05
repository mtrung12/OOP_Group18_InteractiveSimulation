package model;

public class Cube extends PhysicalObject{
    private double sideLength;
    
    public Cube() {
        
    }

    public Cube(double mass, double sideLength) {
        super(mass);
        this.sideLength = sideLength;
    }

    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(double sideLength) {
        this.sideLength = sideLength;
    }
    

}