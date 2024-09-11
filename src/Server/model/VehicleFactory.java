package Server.model;

public class VehicleFactory {

    public static Vehicle createVehicle(String s){

        if(s.equals("car")) return new Car();
        else if(s.equals("bus")) return new Bus();
        else if(s.equals("truck")) return new Truck();
        else return null;
    }
}
