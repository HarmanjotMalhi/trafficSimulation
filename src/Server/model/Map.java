package Server.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

public class Map implements Component{

    RoadSegment[][] roadSegments;
    public Intersection[][] intersections;
    Supplier<Integer> randomIntSupplier;
    HashMap<String, RoadSegment> vehicleListR;
    HashMap<String, Integer[]> vehicleListP;

    public Map() throws IOException, ParserConfigurationException, SAXException {
        roadSegments = new RoadSegment[51][51];
        intersections = new Intersection[51][51];
        initializeMap();
        randomIntSupplier = () -> new Random().nextInt((51 - 0) + 1);
        vehicleListR = new HashMap<>();
        vehicleListP = new HashMap<>();

    }

    public void addVehicle(Vehicle v){

        int road = randomIntSupplier.get();
        int roadSegment = randomIntSupplier.get();

        while(roadSegment % 2 != 0){
            roadSegment = randomIntSupplier.get();
        }





        v.changeDirection(roadSegments[road][roadSegment].getDirection());
        roadSegments[road][roadSegment].addVehicle(v);

        vehicleListR.put(v.plate,roadSegments[road][roadSegment]);
        Integer[] s = new Integer[]{road,roadSegment};

        vehicleListP.put(v.plate,s);

    }

    public int updatePosition(Vehicle v){

        RoadSegment r = vehicleListR.get(v.plate);
        int u = Integer.parseInt(r.changePosition(v));
        return u;
    }

    public String calculateDirection(String direction, String way){

        String[] directions = new String[4];
        directions[0] = "north";
        directions[1] = "east";
        directions[2] = "south";
        directions[3] = "west";

        int index = 0;
        for(int i = 0; i < 4; i++){
            if(directions[i].equals(direction)){
                index = i;
                break;
            }
        }

        if(way.equals("left")) {
            if(index > 0) return directions[index-1];
            else if(index == 0) return directions[3];
        }

        if(way.equals("right")){
            if(index<3) return directions[index+1];
            else if(index == 3) return directions[0];
        }

        return "";

    }


    public boolean transferVehicle(Vehicle v, String direction){

        Integer[] s = vehicleListP.get(v.plate);

        int row = s[0];
        int column = s[1];



        String color = intersections[row][column+1].getLight(v.getCurrentDirection());


        if(color.equals("GREEN")) {
            RoadSegment r = vehicleListR.get(v.plate);
            r.delete(v);
            if(direction.equals("left")) {
                v.changeDirection(roadSegments[row-1][column+2].direction2);
                roadSegments[row-1][column+2].addVehicle(v);
            }
            else if(direction.equals("right")) {
                v.changeDirection(roadSegments[row+1][column+2].direction2);
                roadSegments[row+1][column+2].addVehicle(v);
            }
            else if(direction.equals("front")) {
                v.changeDirection(roadSegments[row][column+2].direction2);
                roadSegments[row][column+2].addVehicle(v);
            }
            updateHashmap(direction,v);
            return true;
        }
        else{
            System.out.println("Sorry Bro!, the light is " + color);
            v.setSpeed(0);
            return false;
        }

    }

    public void updateHashmap(String d, Vehicle v){

        Integer[] s = vehicleListP.get(v.plate);

        int row = s[0];
        int column = s[1];
        if(d == "front") {
            Integer[] temp = new Integer[]{row,column+2};
            vehicleListP.put(v.plate, temp);
            vehicleListR.put(v.plate,roadSegments[row][column+2]);
        }
        else if(d == "right") {
            Integer[] temp = new Integer[]{row+1,column+2};
            vehicleListP.put(v.plate, temp);
            vehicleListR.put(v.plate,roadSegments[row+1][column+2]);
        }

        else if(d == "left") {
            Integer[] temp = new Integer[]{row-1,column+2};
            vehicleListP.put(v.plate, temp);
            vehicleListR.put(v.plate,roadSegments[row-1][column+2]);
        }
    }

    public String[] getPosition(Vehicle v) throws VehicleNotFoundException{

        try {
            String[] encoded = new String[4];

            Integer[] i = vehicleListP.get(v.plate);
            int row = i[0];
            int column = i[1];
            encoded[0] = String.valueOf(row);
            encoded[1] = String.valueOf(column);


            String rs_info = vehicleListR.get(v.plate).currentPosition(v);
            String lane = String.valueOf(rs_info.charAt(0));
            encoded[2] = lane;

            String position_in_lane = "";
            for (int count = 1; count < rs_info.length(); count++) {
                position_in_lane = position_in_lane + rs_info.charAt(count);
            }
            encoded[3] = position_in_lane;
            return encoded;
        }
        catch(VehicleNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;


    }

    public void changeLanes(Vehicle v, int i) throws VehicleAlreadyExistsException {

        RoadSegment r = vehicleListR.get(v.plate);

        r.changeLanes(v,i);
    }

    public int challenge(Vehicle v){

        Integer[] i = vehicleListP.get(v.plate);
        int row = i[0];
        int column = i[1];

        Intersection ci = intersections[row][column+1];

        ci.changeLights();

        if(ci.getLight(v.getCurrentDirection()).equals("GREEN")) return 1;
        else return 0;
    }

    public int[] checkSurroundings(Vehicle v){

        RoadSegment r = vehicleListR.get(v.plate);
        return r.checkSurroundings(v);
    }

    public <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public String status() throws VehicleNotFoundException {

        String s = "Current vehicle with location, Current road segments used status and their lanes status and next intersection status: " + '\n';
        for (HashMap.Entry<String, Integer[]> entry : vehicleListP.entrySet()) {

            Integer [] location = entry.getValue();
            s = s + "Vehicle: " + entry.getKey() + " location in map: " + location[0] + " " + location[1] + '\n' + " Current Road Segment status: " + roadSegments[location[0]][location[1]].status() + '\n' + " Next Light: " + intersections[location[0]+1][location[1]+1].status();
        }

        return s;

    }

























    public void initializeMap() throws IOException, ParserConfigurationException, SAXException {

        FileReader f = new FileReader("/Users/harman/Desktop/Assignment2/src/simulation/continuous_1010.txt");
        BufferedReader bf = new BufferedReader(f);

        FileReader sf = new FileReader("/Users/harman/Desktop/Assignment2/src/simulation/continuous_0101.txt");
        BufferedReader sbf = new BufferedReader(sf);

        DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1 = factory1.newDocumentBuilder();

        Document document1 = builder1.parse("yes_no_pattern.xml");

        Element root1 = document1.getDocumentElement();

        NodeList rows1 = document1.getElementsByTagName("row");

        for(int i = 0; i < rows1.getLength(); i++){
            Node row = rows1.item(i);
            NodeList children = row.getChildNodes();

            for(int j = 0, col = 0; j < children.getLength(); j++){
                Node node = children.item(j);
                if(node instanceof Element) {
                    Element element = (Element) node;

                    if("yes".equals(element.getTagName()) && i % 2 == 0){
                        roadSegments[i][col] = new RoadSegment("north","south");

                    }else if("yes".equals(element.getTagName()) && i % 2 == 1){
                        roadSegments[i][col] = new RoadSegment("east","west");
                    }else{
                        roadSegments[i][col] = null;
                    }
                    col++;
                }
            }
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse("output.xml");


        Element root = document.getDocumentElement();
        //System.out.println("Root Element: " + root.getNodeName());


        NodeList rows = document.getElementsByTagName("row");

        for (int i = 0; i < rows.getLength(); i++){
            Node row = rows.item(i);
            NodeList children = row.getChildNodes();

            for(int j = 0, col = 0; j < children.getLength(); j++){

                Node node = children.item(j);
                if(node instanceof Element){
                    if("intersection".equals(node.getNodeName())){
                        intersections[i][j] = new Intersection(roadSegments[i][j+1],roadSegments[i+1][j+1],roadSegments[i][j-1],roadSegments[i-1][j+1]);
                    }
                    else if("null".equals(node.getNodeName())){
                        intersections[i][j] = null;
                    }
                    else if("fintersection".equals(node.getNodeName())){

                        intersections[i][j] = new Intersection(roadSegments[i][j+1],
                                roadSegments[i+1][j+1]
                                ,roadSegments[i][j-1],
                                null);
                    }
                    else if("lintersection".equals(node.getNodeName())){
                        intersections[i][j] = new Intersection(roadSegments[i][j+1],null,roadSegments[i][j-1],roadSegments[i-1][j+1]);
                    }
                }
            }
        }



    }
}

