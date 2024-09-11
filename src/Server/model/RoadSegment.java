package Server.model;

import java.util.HashMap;
import java.util.function.BinaryOperator;

public class RoadSegment {

    Lane[] lanes1;
    Lane[] lanes2;
    String roadId;
    String direction1;
    public String direction2;
    HashMap<String, Integer> vehicleList = new HashMap<>();


    public RoadSegment(String one, String two) {
        direction1 = one;
        direction2 = two;
        lanes1 = new Lane[3];
        lanes2 = new Lane[3];
        for(int i = 0; i < 3; i++){
            lanes1[i] = new Lane(500);
            lanes1[i].direction = one;
        }
        for(int i = 0; i < 3; i++){
            lanes2[i] = new Lane(500);
            lanes2[i].direction = two;
        }
    }

   public  boolean addVehicle(Vehicle v){
        try {
            if (v.getCurrentDirection().equals(lanes1[0].direction)) {
                for (int i = 0; i < lanes1.length; i++) {
                    if (lanes1[i].canEnter()) {
                        lanes1[i].addVehicle(v);
                        vehicleList.put(v.plate, i);
                        return true;
                    }
                }
            } else if (v.getCurrentDirection().equals(lanes2[0].direction)) {
                for (int i = 0; i < lanes2.length; i++) {
                    if (lanes2[i].canEnter()) {
                        lanes2[i].addVehicle(v);
                        vehicleList.put(v.plate, i);
                        return true;
                    }
                }
            }
        }
        catch(VehicleAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String changePosition(Vehicle v){
        try {
            int lane = vehicleList.get(v.plate);
            int i;
            BinaryOperator<String> concat = (a, b) -> a + b;
            if (lanes1[0].direction.equals(v.getCurrentDirection())) {
                i = lanes1[lane].changeVehiclePosition(v.plate, v.getSpeed());
                if (i != -1 && i != -2) {
                    String s = concat.apply(String.valueOf(lane), String.valueOf(lanes1[lane].getVehiclePosition(v.plate)));
                    return s;
                } else {
                    return String.valueOf(i);
                }
            } else if (lanes2[0].direction.equals(v.getCurrentDirection())) {
                i = lanes2[lane].changeVehiclePosition(v.plate, v.getSpeed());
                if (i != -1 && i != -2) {
                    String s = concat.apply(String.valueOf(lane), String.valueOf(lanes2[lane].getVehiclePosition(v.plate)));
                    return s;
                } else {
                    return String.valueOf(i);
                }
            }
        }
        catch (VehicleNotFoundException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean changeLanes(Vehicle v, int changeTo) throws VehicleAlreadyExistsException {

        try {
            if (changeTo >= 0 && changeTo <= 2) {
                if (v.getCurrentDirection().equals(lanes1[0].direction)) {
                    int lane = vehicleList.get(v.plate);
                    int position = lanes1[lane].getVehiclePosition(v.plate);
                    lanes1[changeTo].addVehicle(v, position);
                    lanes1[lane].removeVehicle(v);
                    vehicleList.put(v.plate, changeTo);
                } else if (v.getCurrentDirection().equals(lanes2[0].direction)) {
                    int lane = vehicleList.get(v.plate);
                    int position = lanes2[lane].getVehiclePosition(v.plate);
                    lanes2[changeTo].addVehicle(v, position);
                    lanes2[lane].removeVehicle(v);
                    vehicleList.put(v.plate, changeTo);
                }
                return true;
            }
        }
        catch (VehicleAlreadyExistsException | VehicleNotFoundException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String currentPosition(Vehicle v) throws VehicleNotFoundException{

        try {
            if (v.getCurrentDirection().equals(lanes1[0].direction)) {
                return String.valueOf(vehicleList.get(v.plate)) + String.valueOf(lanes1[vehicleList.get(v.plate)].getVehiclePosition(v.plate));
            } else if (v.getCurrentDirection().equals(lanes2[0].direction)) {
                return String.valueOf(vehicleList.get(v.plate)) + String.valueOf(lanes2[vehicleList.get(v.plate)].getVehiclePosition(v.plate));
            }
        }
        catch(VehicleNotFoundException e){
            System.out.println(e.getMessage());
        }

       return "  ";
    }

    public String getDirection(){

        return direction1;
    }

    public void delete(Vehicle v){

        if(v.getCurrentDirection().equals(lanes1[0].direction)){
            int lane = vehicleList.get(v.plate);
            lanes1[lane].removeVehicle(v);
        }
        else if(v.getCurrentDirection().equals(lanes2[0].direction)){
            int lane = vehicleList.get(v.plate);
            lanes2[lane].removeVehicle(v);
        }
        vehicleList.remove(v.plate);
    }

    public String status() throws VehicleNotFoundException {

        String s = "Direction: " + direction1 + " " + direction2;
        int i = 0;


        for (HashMap.Entry<String, Integer> entry : vehicleList.entrySet()) {

            s = s + "Vehicle Id : " + entry.getKey() + ", current lane: " + entry.getValue() + '\n';

            if(lanes2[entry.getValue()].getVehiclePosition(entry.getKey()) != -1){
                s = s + "Current lane status: " + lanes2[entry.getValue()].status();
            } else if(lanes1[entry.getValue()].getVehiclePosition(entry.getKey()) != -1){
                s = s + "Current lane status: " + lanes1[entry.getValue()].status();
            }

            i++;
        }

        if(i < 20) s = s + " road less busy";
        if( i > 20) s = s + " road busy";

        return s;

    }



    public int[] checkSurroundings(Vehicle v){

        try {
            int[] ret = new int[4];
            int number = vehicleList.get(v.plate);
            if (v.getCurrentDirection().equals(lanes1[0].direction)) {
                int positionInLane = lanes1[number].getVehiclePosition(v.plate);
                Vehicle[] l = lanes1[number].lane;
                if (positionInLane + 1 < 500 && l[positionInLane + 1] == null) {
                    ret[0] = 1;
                } else{
                    ret[0] = 0;
                }
                if (positionInLane - 1 >= 0 && l[positionInLane - 1] == null) {
                    ret[1] = 1;
                } else{
                    ret[1] = 0;
                }
                if (number == 1) {
                    ret[2] = 0;
                    Vehicle[] left = lanes1[number + 1].lane;
                    if (left[positionInLane] == null) {
                        ret[3] = 1;
                    } else{
                        ret[3] = 0;
                    }
                }

                if (number == 3) {
                    ret[3] = 0;
                    Vehicle[] left = lanes1[number - 1].lane;
                    if (left[positionInLane] == null) {
                        ret[3] = 1;
                    } else{
                        ret[3] = 0;
                    }
                }
            } else if (v.getCurrentDirection().equals(lanes2[0].direction)) {
                int positionInLane = lanes2[number].getVehiclePosition(v.plate);
                Vehicle[] l = lanes2[number].lane;
                if (positionInLane + 1 < 500 && l[positionInLane + 1] == null) {
                    ret[0] = 1;
                } else {
                    ret[0] = 0;
                }
                if (positionInLane - 1 > 0 && l[positionInLane - 1] == null) {
                    ret[1] = 1;
                } else {
                    ret[1] = 0;
                }
                if (number == 0) {
                    ret[2] = 0;
                    Vehicle[] left = lanes2[number + 1].lane;
                    if (left[positionInLane] == null) {
                        ret[3] = 1;
                    } else{
                        ret[3] = 0;
                    }
                }

                if (number == 2) {
                    ret[2] = 0;
                    Vehicle[] left = lanes2[number - 1].lane;
                    if (left[positionInLane] == null) {
                        ret[3] = 1;
                    } else{
                        ret[3] = 0;
                    }
                }
                if (number == 1) {
                    Vehicle[] left = lanes2[number - 1].lane;
                    if (left[positionInLane] == null) {
                        ret[3] = 1;
                    } else{
                        ret[3] = 0;
                    }
                    Vehicle[] right = lanes2[number + 1].lane;
                    if (right[positionInLane] == null) {
                        ret[2] = 1;
                    } else{
                        ret[2] = 0;
                    }
                }
            }
            return ret;
        }

        catch(VehicleNotFoundException e){
            System.out.println(e.getMessage());
        }

        return null;

    }


}
