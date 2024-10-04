package assignment2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;
import java.net.*;

public class GETClient {

    private GETClient() {} // This is changed

    public static void main(String[] args) throws InterruptedException {

        String host;
        int attempts = 5;
        if(args.length < 1) {
        	host = null;
        } else {
        	host = args[0];
        }
        try {
            Registry registry = LocateRegistry.getRegistry(2000);
            
            //These 2 lines of code were change
            AggregationInterface stub = (AggregationInterface) registry.lookup("AggregationInterface");
            String response = stub.helloTest();
            
            System.out.println("RMI server response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        while(attempts > 0) {
        try {   	
        	 Socket socket;
	        	if(args.length > 0) {
	        		Integer port = Integer.valueOf(args[0]);
	        		socket = new Socket("localhost",port);
	        	} else {
	        		// Create a socket that listens on default port 4567
	        		socket = new Socket("localhost",4567);  		
	        	}
        	// Connect and send GET to Server
            // Send message to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET");

            // Receive response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            while(true) {
            	String response = in.readLine();
            	if(response != null) {
            		System.out.println(response);	            		
            	}
            }
        } catch (IOException e) {
        	System.out.println("Error occurred. Retrying connection");
        	attempts--;
        	Thread.sleep(5000);
            e.printStackTrace();
        	}
        }
        
    }
}