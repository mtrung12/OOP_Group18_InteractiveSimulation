package model;

public class PhysicalObject {
    private double mass;

    
    public PhysicalObject() {
    }

    public PhysicalObject(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
    
}
