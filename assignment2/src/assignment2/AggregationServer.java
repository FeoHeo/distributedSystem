package assignment2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class AggregationServer implements AggregationInterface {

	public AggregationServer() {};
	
	@Override
	public String helloTest() throws RemoteException {
		return "Hello, this is a test";
	}

public static void main(String args[]) {
        
        try {
            AggregationServer obj = new AggregationServer();
            AggregationInterface stub = (AggregationInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(2000);
            registry.bind("AggregationInterface", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
