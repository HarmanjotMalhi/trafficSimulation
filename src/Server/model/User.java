package Server.model;

public class User implements Player, Component {


    Vehicle v;
    int Reputation;

    public User(String i) {
        v = VehicleFactory.createVehicle(i);
        Reputation = 100;
    }
    @SuppressWarnings("unchecked")

    @Override
    public void accelerate(int i) {
        v.accelerate(i);
    }


    public Vehicle getVehicle(){
        return v;
    }

    public void addToReputation(int i){
        Reputation = Reputation + i;
    }

    public int getReputation(){
        return Reputation;
    }

    @Override
    public String status() {
        return String.valueOf("Current reputation " + Reputation + " " + v.status());
    }
}
