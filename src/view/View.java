package view;

import java.io.Serializable;
import java.util.Scanner;

public class View implements Serializable {

    transient Scanner scanner;
    public String vehicle;
    public int quit;
    public int accelerate;
    public int wantLane;
    public int laneNo;
    public int checkSN;
    public int[] surroundings;
    public int[] position;
    public int vehicleSpeed;
    public int vehicleAcceleration;
    public int reputation;
    public int challenge;
    public int way;
    public String turn;
    public int status;


    public View(){

        scanner = new Scanner(System.in);
    }

    public void chooseVehicle(){
        System.out.print("Type car for a car vehicle, bus for bus, truck for truck: ");
        vehicle = scanner.next();
        System.out.println();System.out.println();

    }

    public void makeUserEnterGame(){

        System.out.print("Enter 1 to enter game: ");
        int i = scanner.nextInt();
        System.out.println();
        while( i != 1 ) {
            System.out.print("Enter 1 to enter game: ");
            i = scanner.nextInt();
            System.out.println();
        }
    }

    public void quit(){
        System.out.print("Enter 0 to quit: ");
        quit = scanner.nextInt();
        System.out.println();System.out.println();
    }

    public void howMuchAccelerate(){
        System.out.print("Increase acceleration by: ");
        accelerate = scanner.nextInt();
        System.out.println();System.out.println();
    }

    public int wantToChangeLanes(){

        System.out.print("Type 1 to change lanes: ");
        wantLane = scanner.nextInt();
        return wantLane;
    }

    public void toWhatLane(){
        System.out.print("To what lane, enter between 0 <= 2: ");
        laneNo = scanner.nextInt();
        System.out.println();System.out.println();
    }

    public void checkSurrounding(){
        System.out.print("Press 1 to Check surroundings: ");
        checkSN = scanner.nextInt();
        System.out.println();System.out.println();
    }

    public void surroundings(){
        if(surroundings[0] == 0) System.out.println("Front is not clear");
        else if(surroundings[0] == 1) System.out.println("front is clear");
        if(surroundings[1] == 0) System.out.println("Back is not clear");
        else if(surroundings[1] == 1) System.out.println("Back is clear");
        if(surroundings[2] == 0) System.out.println("Right is not clear");
        else if(surroundings[2] == 1) System.out.println("Right is clear");
        if(surroundings[3] == 0) System.out.println("left is not clear");
        else if(surroundings[3] == 1) System.out.println("left is clear");
    }

    public void wantToChallenge(){
        System.out.print("Press 1 to challenge: ");
        challenge = scanner.nextInt();
        System.out.println();System.out.println();

    }

    public void intersectionTime(){
        System.out.print("At the intersection, enter 1 for left, 2 for straight, 3 for right");
        way = scanner.nextInt();
        System.out.println();System.out.println();

    }


    public void addBlankSpace(){
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void wantStatus(){
        System.out.println("Type 1 for status: ");
        status = scanner.nextInt();
        System.out.println();System.out.println();
    }
}
