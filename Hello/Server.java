package Hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
	
public class Server {
	
	
    public static void main(String args[]) {
	
	try {
	    Interface obj = new Interface();

	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.createRegistry(2000); //Port number, bind client to this
	    registry.bind("Hello", obj);

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}