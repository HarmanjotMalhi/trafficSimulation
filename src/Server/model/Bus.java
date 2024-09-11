package Server.model;

public class Bus extends Vehicle{

    public static final int max_accel = 5;
    public Bus(Vehicle vehicle) {
        super();
    }

    public Bus() {
    }

    public int getMaxSpeed(){
        return 130;
    }

    @Override
    public int getMaxAcceleration() {
        return 45;
    }
}
