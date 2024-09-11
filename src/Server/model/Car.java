package Server.model;

public class Car extends Vehicle{

    public Car(Vehicle vehicle) {
        super();
    }

    public Car() {
    }

    public int getMaxSpeed(){
        return 150;
    }

    @Override
    public int getMaxAcceleration() {
        return 60;
    }

}
