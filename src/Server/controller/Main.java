package Server.controller;

import Server.model.*;
import org.xml.sax.SAXException;
import view.View;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class Main {

    Vehicle vehicle;
    User user;
    Map map;


    public Main(View view) throws IOException, ParserConfigurationException, SAXException {

        map = new Map();
        user = new User(view.vehicle);
        vehicle = user.getVehicle();
        vehicle.plate = "Malhi13";
        map.addVehicle(vehicle);
        System.out.println("executed");

    }

    public String doStuff(View view) throws VehicleAlreadyExistsException, VehicleNotFoundException {

        String ret = "";

        vehicle.accelerate(view.accelerate);

        if(view.wantLane==1){
            map.changeLanes(vehicle,view.laneNo);
        }

        if(view.checkSN == 1){
            view.surroundings = map.checkSurroundings(vehicle);
        }

        if(view.challenge == 1) {
            if (map.challenge(vehicle) == 1) {
                user.addToReputation(10);
            }
            else{
                user.addToReputation(-10);
            }
        }

        int i = updatePosition();
        if(i == -2) return String.valueOf(-2);

        ret = ret + presentStatus(map.getPosition(vehicle),vehicle,user);
        if(view.status == 1){
            ret = ret + user.status() + map.status();
        }
        if(view.checkSN == 1){
            ret = ret + surroundings(map.checkSurroundings(vehicle));
        }

        return ret;

    }

    public String presentStatus(String[] position, Vehicle v, User u){

        return "Road: " + position[0] + "   " + "RoadSegment: " + position[1] + '\n' +
        "Lane: " + position[2] + "   " + "Position in the lane: " + position[3] + '\n' +
        "Speed: " + v.getSpeed() + "   " + "Acceleration: " + v.getAcceleration() + '\n' +
        "Reputation: " + u.getReputation() + '\n' +  '\n' ;

    }

    public String surroundings(int[] surroundings){

        String ret = "";
        if(surroundings[0] == 0) ret = ret + "Front is not clear" + '\n' ;
        else if(surroundings[0] == 1) ret = ret + "front is clear" + '\n' ;
        if(surroundings[1] == 0) ret = ret + "Back is not clear" + '\n' ;
        else if(surroundings[1] == 1) ret = ret + "Back is clear" + '\n' ;
        if(surroundings[2] == 0) ret = ret + "Right is not clear" + '\n' ;
        else if(surroundings[2] == 1) ret = ret + "Right is clear" + '\n' ;
        if(surroundings[3] == 0) ret = ret + "left is not clear" + '\n' ;
        else if(surroundings[3] == 1) ret = ret + "left is clear" + '\n' ;

        return ret;
    }

    public int updatePosition(){

        if(map.updatePosition(vehicle) == -2) return -2;
        else return 0;
    }

    public void transfer(String s){
        if( s.equals("left")) {
            map.transferVehicle(vehicle,"left");
            System.out.println("transferred");
        }
        else if(s.equals("front")){
            map.transferVehicle(vehicle,"front");
            System.out.println("transferred");
        }
        else if(s.equals("right")) {
            map.transferVehicle(vehicle,"right");
            System.out.println("transferred");
        }
    }

    public void changeLights(){
        for(Intersection[] ia : map.intersections){
            for(Intersection temp : ia){
                if(temp != null) temp.changeLights();
            }
        }
    }

    }

