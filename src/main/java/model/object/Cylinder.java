package model.object;

import model.force.Force;
import model.surface.Surface;

public class Cylinder extends PhysicalObject {
    private double radius;
    private double angularAcceleration;
    private double angularVelocity;
    private double angularPosition;
    public Cylinder(double mass, double height, Surface surface, Force appliedForce) {
        super(mass, height, surface, appliedForce);
        this.radius = height/2;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    @Override
    public double getAngularAcceleration() {
        return angularAcceleration;
    }
    @Override
    public double getAngularVelocity() {
        return angularVelocity;
    }
    @Override
    public double getAngularPosition() {
        return angularPosition;
    }
    public void calculateAngularAcceleration(){
        angularAcceleration = super.getFrictionValue()*radius/(0.5*radius*radius*getMass());
    }

    public void calculateAngularVelocity(double t){
        angularVelocity +=+ this.getAngularAcceleration()*t;
    }

    public void calculateAngularPosition(double t){
        angularPosition += angularVelocity*t;
    }
    @Override
    public void frictionUpdate(){
        double appliedValue = appliedForce.getValue();
        double normalValue = normalForce.getValue();
        double staticCoefficient = surface.getStaticFrictionCoefficient();
        double kineticCoefficient = surface.getKineticFrictionCoefficient();
        if(Math.abs(appliedValue)<=Math.abs(3*normalValue*staticCoefficient)){
            if(super.getVelocity()>=0){
                friction.setValue(-appliedValue/3);
            }
            else {
                friction.setValue(appliedValue / 3);
            }
        }
        else{
            if(super.getVelocity()>=0){
                friction.setValue(-normalValue*kineticCoefficient);
            }
            else {
                friction.setValue(normalValue * kineticCoefficient);
            }
        }
    }

    @Override
    public void update(double t){
        super.update(t);
        calculateAngularAcceleration();
        calculateAngularVelocity(t);
        calculateAngularPosition(t);
    }
}
