package model.object;

import model.force.Force;
import model.surface.Surface;

public class Cube extends PhysicalObject {

    public Cube(double mass, double height, Surface surface, Force appliedForce) {
        super(mass, height, surface, appliedForce);
    }

    @Override
    public void frictionUpdate(){
        double appliedValue = appliedForce.getValue();
        double normalValue = normalForce.getValue();
        double staticCoefficient = surface.getStaticFrictionCoefficient();
        double kineticCoefficient = surface.getKineticFrictionCoefficient();
        if(Math.abs(appliedValue)<=Math.abs(normalValue*staticCoefficient)){
            if(super.getVelocity()>=0){
                friction.setValue(-appliedValue);
            }
            else{
                friction.setValue(appliedValue);
            }
        }
        else{
            if(super.getVelocity()>=0){
                friction.setValue(-normalValue*kineticCoefficient);
            }
            else{
                friction.setValue(normalValue*kineticCoefficient);
            }
        }
    }
}