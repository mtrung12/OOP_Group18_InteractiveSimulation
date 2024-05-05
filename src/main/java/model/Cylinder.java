package model;

public class Cylinder extends PhysicalObject{
    private double radius;
    

    public Cylinder(double mass) {
    }

    public Cylinder(double mass, double radius) {
        super(mass);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    
}
