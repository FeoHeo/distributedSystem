package assignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AggregationInterface extends Remote {
	String helloTest() throws RemoteException;
}
