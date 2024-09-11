package Server.model;

public abstract class Vehicle implements Component {

    public String plate;

    private int speed;

    private Boolean engineOn;

    private int acceleration;

    private int position;

    private String Direction;

    private String Color;

    private int Health;

    private int reputation;

    public abstract int getMaxSpeed();

    public abstract int getMaxAcceleration();

    public void drive() {
    }


    interface AccelerationCalculator {
        int calculate(int currentAcceleration, int increment);
    }

    public void accelerate(int value) {
        AccelerationCalculator calculator = (currentAcceleration, increment) -> {
            if (currentAcceleration + increment > getMaxAcceleration()) {
                return getMaxAcceleration();
            } else {
                return currentAcceleration + increment;
            }
        };

        acceleration = calculator.calculate(getAcceleration(), value);
        if(speed + acceleration < getMaxSpeed())
        speed = speed + acceleration;
        else{
            speed = getMaxSpeed();
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getPosition() {
        return position;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getReputation(){return reputation; }

    public String getColor(){return Color;}

    public int getHealth(){return Health;}

    public String getCurrentDirection(){return Direction;}

    public boolean engineOn(){ return engineOn;}


    public void changeDirection(String direction) {
        Direction = direction;
    }

    public void setSpeed(int s){
        speed = s;
    }

    public String status(){
        return String.valueOf(plate + "'s Direction: " + getCurrentDirection() + " Speed: " + speed + ", Acceleration: " + acceleration);
    }


}
