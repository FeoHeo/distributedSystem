package Hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {
	try {
	    Registry registry = LocateRegistry.getRegistry("127.0.0.1",2000);
	    Hello stub = (Hello) registry.lookup("Hello");
	    String response = stub.sayHello();
	    System.out.println("response: " + response);
		stub.pushValue(1);
		stub.pushValue(2);
		System.out.println(stub.displayStack());
		stub.pop();
		System.out.println(stub.displayStack());
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}
