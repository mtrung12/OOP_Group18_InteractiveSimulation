package model.object;

import model.force.Force;
import model.surface.Surface;

public abstract class PhysicalObject {
    private double mass;
    //(cube) height=side length, (cylinder) height=2*radius    
    private double height;
    //(x,y) is the position of center of the object
    //(0,0) is the center of the initial point of contact between the object and the surface
    private double x;
    private double y;
    protected Surface surface;
    protected Force appliedForce;
    protected Force friction;
    protected Force normalForce;
    private double acceleration;
    private double velocity;


    public PhysicalObject(double mass, double height, Surface surface, Force appliedForce){
        this.mass = mass;
        this.height=height;
        this.x = 0;
        this.y = height/2;
        this.surface = surface;
        this.appliedForce = appliedForce;
        this.normalForce = new Force(mass*10);
        this.friction = new Force(0);
        this.acceleration=0;
        this.velocity=0;
    }

    public void frictionUpdate(){

    };


    public void accelerationUpdate(){
        acceleration = getResulantForceValue()/mass;
    }

    public void velocityUpdate(double t){
        velocity += acceleration*t;
    }


     public void positionUpdate(double t) {
         x += velocity * t;
     }

     public double getPosition(){
         return x;
     }

     public void update(double t){
         frictionUpdate();
         accelerationUpdate();
         velocityUpdate(t);
         positionUpdate(t);
     }

    public double getMass() {
        return mass;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getAngularAcceleration(){
        return 0;
    }

    public double getAngularVelocity(){
        return 0;
    }

    public double getAngularPosition(){
        return 0;
    }
    public double getVelocity() {
        return velocity;
    }

    public void setAppliedForce(Force appliedForce) {
        this.appliedForce = appliedForce;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public double getResulantForceValue(){
        return appliedForce.getValue()+friction.getValue();
    }

    public double getAppliedForceValue(){
        return appliedForce.getValue();
    }

    public double getFrictionValue(){
        return friction.getValue();
    }
}
