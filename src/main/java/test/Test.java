package test;

import model.object.Cube;
import model.force.Force;
import model.object.Cylinder;
import model.surface.Surface;

public class Test {
    public static void main(String[] args){
        Force force = new Force(20);
        Surface surface = new Surface(0.5, 0.3);
        Cube cube = new Cube(1, 10, surface, force);
        Cylinder cylinder = new Cylinder(1, 2, surface, force);
        while(true){
            cylinder.update(0.1);
            System.out.println("Angular Velocity: "+cylinder.getAngularVelocity());
        }


    }
}
