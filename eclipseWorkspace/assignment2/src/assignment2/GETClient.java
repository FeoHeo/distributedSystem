package assignment2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GETClient {

    private GETClient() {} // This is changed

    public static void main(String[] args) {

        String host
        if(args.length < 1) {
        	host = null;
        } else {
        	host = args[0]
        }
        try {
            Registry registry = LocateRegistry.getRegistry(2000);
            
            //These 2 lines of code were change
            AggregationInterface stub = (AggregationInterface) registry.lookup("AggregationInterface");
            String response = stub.helloTest();
            
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}