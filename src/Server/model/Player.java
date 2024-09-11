package Server.model;

public interface Player {

    Vehicle v = null;

    public default void accelerate(int i){}

    public default void deaccelerate(int i){}

    public default void challenge(){}
}
