package Server.model;

public class VehicleAlreadyExistsException extends Exception {

    // Constructor without parameters
    public VehicleAlreadyExistsException() {
        super("Vehicle already exists.");
    }

    // Constructor that accepts a message
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
