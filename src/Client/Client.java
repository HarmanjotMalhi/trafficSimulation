package Client;

import view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void setView(View V){

        V.quit();
        V.howMuchAccelerate();
        V.wantToChangeLanes();
        if(V.wantLane == 1)
            V.toWhatLane();
        V.checkSurrounding();
        V.wantToChallenge();
        V.wantStatus();


    }

    public static void main(String args[]) throws IOException {

        Scanner scanner = new Scanner(System.in);
        View v = new View();
        v.makeUserEnterGame();
        v.chooseVehicle();





        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");

        byte [] buffer = new byte[65000];
        DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
        DatagramSocket receiveSocket = new DatagramSocket(8900);


        while(true) {
            setView(v);

            if(v.quit == 0) break;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(v);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();


            DatagramPacket packet = new DatagramPacket(data, data.length, address, 8960);
            socket.send(packet);


            receiveSocket.receive(receive);
            String receivedString = new String(receive.getData(), 0, receive.getLength());
            if(receivedString.equals("-2")){
                System.out.print("At the intersection, type left to go left, type right to go right, front to go straight");
                String s = scanner.next();
                byte[] msg = s.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet1 = new DatagramPacket(msg, msg.length, address, 8960);
                socket.send(packet1);
            }
            else{
                System.out.println(receivedString);
            }
        }


    }
}
