package Server.model;

public class Truck extends Vehicle{
    public Truck(Vehicle vehicle) {
        super();
    }

    public Truck() {
    }

    public int getMaxSpeed(){
        return 100;
    }

    @Override
    public int getMaxAcceleration() {
        return 30;
    }
}
