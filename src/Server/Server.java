package Server;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import Server.controller.Main;
import Server.model.VehicleAlreadyExistsException;
import Server.model.VehicleNotFoundException;
import org.xml.sax.SAXException;
import view.View;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {

    static String url = "jdbc:h2:~/test;MODE=MySQL";
    static String usr = "HM";
    static String pass = "";

    public View get(View V){
        return V;
    }

    public static void initializeDatabase() throws ClassNotFoundException, SQLException {



         Class.forName("org.h2.Driver");
         Connection connection = DriverManager.getConnection(url, usr, pass);

         boolean tableExists = false;

         ResultSet results = connection.getMetaData().getTables(null,null,"USERS",null);
         tableExists = results.next();

         if(!tableExists){
             Statement statement = connection.createStatement();
             String sqlCreateTable = "CREATE TABLE USERS " +
                     "(id INTEGER not NULL AUTO_INCREMENT, " +
                     " username VARCHAR(255), " +
                     " password VARCHAR(255), " +
                     " PRIMARY KEY ( id ))";

             statement.executeUpdate(sqlCreateTable);

             String sqlInsertUser = "INSERT INTO USERS (username, password) VALUES ('gulaab', 'jamun')";
             statement.executeUpdate(sqlInsertUser);

          }
    }

    public static boolean verifyUser(String username, String password) throws SQLException {

        String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";

        Connection connection = DriverManager.getConnection(url, usr, pass);
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1,username);
        statement.setString(2,password);

        ResultSet rs = statement.executeQuery();

        if(rs.next()){
            return true;
        }
        return false;
    }



    public static void main(String args[]) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException, VehicleAlreadyExistsException, VehicleNotFoundException, SQLException {

        InetAddress address = InetAddress.getByName("localhost");

        DatagramSocket socket = new DatagramSocket(8960);
        byte [] buffer = new byte[65000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        socket.receive(packet);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(),0, packet.getLength());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        View v = (View)objectInputStream.readObject();
        Main main = new Main(v);


        while(true) {

            String s = main.doStuff(v);

            if(s.equals("-2")){
                byte[] message = s.getBytes(StandardCharsets.UTF_8);
                DatagramPacket datagramPacket = new DatagramPacket(message, message.length, address, 8900);
                socket.send(datagramPacket);
                socket.receive(packet);
                String receivedString = new String(packet.getData(), 0, packet.getLength());
                System.out.println(receivedString);
                main.transfer(receivedString);
            }
            else {
                byte[] message = s.getBytes(StandardCharsets.UTF_8);
                DatagramPacket datagramPacket = new DatagramPacket(message, message.length, address, 8900);
                socket.send(datagramPacket);

                socket.receive(packet);
                ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream objectInputStream1 = new ObjectInputStream(byteArrayInputStream1);
                v = (View) objectInputStream1.readObject();
            }

            main.changeLights();
        }


    }
}
