package model;

public class Surface {
    private double staticFrictionCoefficient;
    private double kineticFrictionCoefficient;
    public Surface() {
    }
    public Surface(double staticFrictionCoefficient, double kineticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
        this.kineticFrictionCoefficient = kineticFrictionCoefficient;
    }
    public double getStaticFrictionCoefficient() {
        return staticFrictionCoefficient;
    }
    public void setStaticFrictionCoefficient(double staticFrictionCoefficient) {
        this.staticFrictionCoefficient = staticFrictionCoefficient;
    }
    public double getKineticFrictionCoefficient() {
        return kineticFrictionCoefficient;
    }
    public void setKineticFrictionCoefficient(double kineticFrictionCoefficient) {
        this.kineticFrictionCoefficient = kineticFrictionCoefficient;
    }

    // public double calculateFrictionalForce(){
    //     double force;
        
    //     return force;
    // }
    
}
