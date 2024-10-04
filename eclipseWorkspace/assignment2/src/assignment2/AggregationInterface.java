package assignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.PrintWriter;

import org.json.simple.JSONObject;

public interface AggregationInterface extends Remote {
	
	
	String helloTest() throws RemoteException;
	
	void startExpungeCounter(int countDown) throws RemoteException;
	
	JSONObject parseJSON(BufferedReader inputStream , PrintWriter outputStream , boolean debug) throws RemoteException;
}
