package assignment2;

import java.rmi.RemoteException; // For Java rmi
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.json.simple.JSONObject; // FOR JSON parsing
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader; // Other Java functionality
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import java.io.InputStreamReader; // Socket stuff
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.TimeUnit; // Manage time to expunge data
import java.time.LocalTime;
import java.time.Duration;


public class AggregationServer implements AggregationInterface {

	public static boolean debug = true;

	public static volatile JSONObject dataJSON = new JSONObject();
	public static boolean dataIsCreated = false;
	public static volatile LocalTime jsonTime;
	public static final JSONObject jsonNull = new JSONObject();
	
	public AggregationServer() {};
	

	public static void main(String args[]) throws InterruptedException {
		int attempts = 5;
		
			try { // Attempt java rmi creation
				AggregationServer obj = new AggregationServer();
				AggregationInterface stub = (AggregationInterface) UnicastRemoteObject.exportObject(obj, 0);
				
				// Bind the remote object's stub in the registry
				Registry registry = LocateRegistry.createRegistry(2000);
				registry.bind("AggregationInterface", stub);
				
				System.err.println("Server ready");
			} catch (Exception e) {
				System.err.println("Server exception: " + e.toString() + ". Retrying server creation");
				e.printStackTrace();
			}	
		
		while(attempts > 0) { 
		try {
			boolean resourceCreated = false; // Use to manage first time creation of ContentServer replica
			
			ServerSocket serverSocket;
			Integer port;
			AggregationServer obj = new AggregationServer();
			if(args.length > 0) {
				port = Integer.valueOf(args[0]);
				serverSocket = new ServerSocket(port);
			} else {
				// Create a server socket that listens on default port 4567
				port = 4567;
				serverSocket = new ServerSocket(4567);  		
			}
			System.out.println("Server is listening on port " + port);
			while (true) {
				// Accept client connection
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected");
				
				// Get input and output streams for reading and writing to the client
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				OutputStream out = clientSocket.getOutputStream();
				PrintWriter writer = new PrintWriter(out, true);
				
				
				// Read the first line of the HTTP request (request line)
				String inputLine = in.readLine();
				
				// Check for request and process it accordingly
				if (inputLine != null && inputLine.startsWith("GET")) {
					// Send an HTTP response header
					writer.println("HTTP/1.1 200 OK");
					writer.println("Content-Type: application/json");
					writer.println("Connection: close");
					writer.println(); // Empty line after headers to signify the start of the body
					// Send the response body (HTML)
					writer.println(dataJSON);
				} else if(inputLine != null && inputLine.startsWith("PUT")) {
					
					
					dataJSON = obj.parseJSON(in,writer,debug); // This function will throw 500 if parsing failed
					if(dataJSON.equals(jsonNull)) {
						writer.println("HTTP/1.1 204 Null JSON data received");
						break;
					}
					jsonTime = LocalTime.now(); // Refresh the time received to reset the expunge counter
					if(resourceCreated) {
						writer.println("HTTP/1.1 200 UPDATED");
						System.out.println("Update received");
					} else {
						System.out.println("Create received");
						writer.println("HTTP/1.1 201 HTTP CREATED");
						resourceCreated = true;
						obj.startExpungeCounter(30); // Can custom set the amount of seconds for debugging
					}
					
				} else {
					// Respond with a 400 Bad Request if it's not a GET or PUT request
					writer.println("HTTP/1.1 400 Bad Request");
				}
				writer.flush();
				
				
				
				
				
				// Close the client socket after responding
				clientSocket.close();
			}
			
		} catch (Exception e) {
			System.out.println("Error occurred. Retrying server creation");
			attempts--;
			Thread.sleep(5000);
			e.printStackTrace();
		}
	}
	}

@Override
public void startExpungeCounter(int countDown) throws RemoteException {
	// TODO Auto-generated method stub
	System.out.println("Expunge counter started");
	Thread backgroundExpunge = new Thread(() -> {
		Duration duration = null;
		JSONObject emptyJSON = new JSONObject();
		try {
			while(true) {
				duration = Duration.between(jsonTime, LocalTime.now());
				if(duration.getSeconds() >= countDown && !dataJSON.equals(emptyJSON)) {
					System.out.println("No contact for " + duration.getSeconds() + " seconds. Expunging expired data");
					dataJSON = new JSONObject();
				}
				Thread.sleep(2000);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	});
	
	backgroundExpunge.start();
	
	
}


public JSONObject parseJSON(BufferedReader inputStream, PrintWriter outputStream , boolean debug) throws RemoteException {
	// TODO Auto-generated method stub
	JSONObject jsonReturn = new JSONObject();
	String temp = null;
	String buffer;
	try { // Save data from buffer to a value
		
	while((buffer = inputStream.readLine()) != null) {
		
		temp = buffer;                
	}
	System.out.println("Printing test: " + temp);
	System.out.println("Test var type: " + temp.getClass().getName());
	} catch (IOException e) {
		System.out.println("An error occurred.");
	      e.printStackTrace();
	}
	
	try { // Parse the JSON data
    	JSONParser parser = new JSONParser();
        // Parse the string into a JSONObject
        jsonReturn = (JSONObject) parser.parse(temp);
        
        // Print the formatted JSONObject
        if(debug) {
        	System.out.println("Formatted JSON: " + jsonReturn.toJSONString());
        	System.out.println("Type of Object: " + jsonReturn.getClass().getName());
        }

    } catch (ParseException e) { // Throws 500 if there is error with parsing
        e.printStackTrace();
        outputStream.println("HTTP/1.1 500 Bad request");
        
    }
	return jsonReturn;
}


@Override
public String helloTest() throws RemoteException { // Use for testing and confirming rmi communication
	return "GET /";
}

}
