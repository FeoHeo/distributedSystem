package Calculator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
	
public class CalculatorServer {
	
	
    public static void main(String args[]) {
	
	try {
	    CalculatorImplementation obj = new CalculatorImplementation();

	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.createRegistry(2000); //Port number, bind client to this
	    registry.bind("Calculator", obj);

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}