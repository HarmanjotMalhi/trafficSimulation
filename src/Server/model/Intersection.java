package Server.model;

public class Intersection {

    Lights eastAndWest;
    Lights northAndSouth;

    RoadSegment north;
    RoadSegment south;
    RoadSegment east;
    RoadSegment west;


    public Intersection(RoadSegment first, RoadSegment second, RoadSegment third, RoadSegment fourth){
        this.north = first;
        this.east = third;
        this.south = second;
        this.west = fourth;
        eastAndWest = new Lights();
        northAndSouth = new Lights();
        northAndSouth.signal = Lights.TrafficLights.GREEN;
        eastAndWest.signal = Lights.TrafficLights.RED;
    }

    public void changeLights(){
        eastAndWest.setTrafficLight(eastAndWest.currentLight());
        northAndSouth.setTrafficLight(northAndSouth.currentLight());
    }

    public String getLight(String direction){

        if(direction.equals("north") || direction.equals("south")) return String.valueOf(northAndSouth.currentLight());
        else if(direction.equals("east") || direction.equals("west")) return String.valueOf(eastAndWest.currentLight());
        else{ return null; }
    }

    public static class Lights {

        enum TrafficLights {
            RED,YELLOW,GREEN
        }
        TrafficLights signal;


        void setTrafficLight(String color){
            if (color.equals("GREEN")) signal = TrafficLights.YELLOW;
            else if (color.equals("YELLOW")) signal = TrafficLights.RED;
            else if(color.equals("RED")) signal = TrafficLights.GREEN;

        }

        String currentLight(){
            return signal.toString();
        }
    }

    public RoadSegment getRoad(String direction){

        if(direction.equals("north")) return north;
        else if(direction.equals("east")) return east;
        else if(direction.equals("west")) return west;

        return null;
    }

    public String status(){

        String s = "north south: " + getLight("north") + " east west: " + getLight("east");
        return s;
    }



}
