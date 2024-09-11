package Server.model;

import java.util.HashMap;

public class Lane{

    public Vehicle[] lane;
    String direction;
    HashMap<String,Integer> vehicleList;

   public  Lane(int length){
        lane = new Vehicle[length];
        for(int i = 0; i < lane.length; i++){
            lane[i] = null;
        }
        vehicleList = new HashMap<>();
    }

    public int changeVehiclePosition(String VehicleID, int howMuch) throws VehicleNotFoundException{

        int temp = getVehiclePosition(VehicleID);

        if(temp == -1){
            return -1;
        }
        else if(temp+howMuch >= lane.length-1){
            return -2;
        }
        else if(roadClear(temp+1,temp+howMuch) != -1){
            int availablePosition = roadClear(temp,temp+howMuch);
            Car cemp = new Car(lane[temp]);
            lane[availablePosition] = cemp;
            lane[temp] = null;
            vehicleList.put(VehicleID,availablePosition);
            return availablePosition;
        }

        else{

            partOfChangePosition(temp,howMuch);
            vehicleList.put(VehicleID,temp+howMuch);
            return temp+howMuch;
        }

    }

    public int getVehiclePosition(String VehicleId) throws VehicleNotFoundException{

       if(vehicleList.containsKey(VehicleId))
        return vehicleList.get(VehicleId);
       else return -1;
    }

    public int roadClear(int temp, int howMuch){

        for(int i = temp; i <= howMuch; i++){
            if(lane[i] != null) return i--;
        }
        return -1;
    }


    private void partOfChangePosition(int temp,int howMuch){

        if(lane[temp] instanceof Car){
            Car cemp = new Car(lane[temp]);
            lane[temp+howMuch] = cemp;
            lane[temp] = null;
        }
        else if(lane[temp] instanceof Bus){
            Bus cemp = new Bus(lane[temp]);
            lane[temp+howMuch] = cemp;
            lane[temp] = null;
        }
        else if(lane[temp] instanceof Truck){
            Truck cemp = new Truck(lane[temp]);
            lane[temp+howMuch] = cemp;
            lane[temp] = null;
        }
    }

    public boolean addVehicle(Vehicle v) throws VehicleAlreadyExistsException{
       if(vehicleList.get(v.plate) != null) {
           throw new VehicleAlreadyExistsException("The specified vehicle already exists in the array.");
       }
        if(lane[0] == null){
            lane[0] = v;
            vehicleList.put(v.plate,0);
            return true;
        }else{
            return false;
        }
    }
    public boolean addVehicle(Vehicle v, int position) throws VehicleAlreadyExistsException{

        if(vehicleList.get(v.plate) != null) {
            throw new VehicleAlreadyExistsException("The specified vehicle already exists in the array.");
        }
        if(lane[position] == null){
            lane[position] = v;
            vehicleList.put(v.plate,position);
            return true;
        }else{
            return false;
        }
    }

    public void removeVehicle(Vehicle v){

       int position = vehicleList.get(v.plate);
       lane[position] = null;
       vehicleList.remove(v.plate);
    }

    public String status(){

       String s = "Vehicle in current Lane";
       int i = 0;
        for (HashMap.Entry<String, Integer> entry : vehicleList.entrySet()) {
            s = s + "Vehicle Id : " + entry.getKey() + ", Position: " + entry.getValue() + '\n';
            i++;
        }

        if(i < 20) s = s + " lane less busy";
        if( i > 20) s = s + " lane busy";

       return s;
    }


    public boolean canEnter(){
        if(lane[0] == null){
            return true;
        }
        else{
            return false;
        }
    }



}
