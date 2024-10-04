package assignment2;

import java.io.*;  // Import the File class
import java.util.Scanner; // Import the Scanner class to read text files
import java.net.*;
import org.json.simple.JSONObject;

public class ContentServer {

	public static void main(String[] args) throws InterruptedException {

		boolean debug = true; // To print extra information when the program is running
		
		int attempts = 5;
		while(attempts > 0) {
			try {
				ContentServer obj = new ContentServer();
				JSONObject jsonFile = new JSONObject();
				String[] data = null;
				
				Socket socket;
				// Create a socket that listens on default port 4567
				if(args.length > 0) {
					Integer port = Integer.valueOf(args[0]);
					socket = new Socket("localhost",port);
				} else {
					socket = new Socket("localhost",4567);  		
				}
				
				while(obj.promptCheck()) { // Will send data when "send" is inputted. Otherwise terminate the program
					
					//Make input, output stream and a File object
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter out = new PrintWriter(socket.getOutputStream() , true);
					File myObj;
					
					if(args.length > 1) { // Default location is on my local computer for convenience in developing
						myObj = new File(args[1]);
					} else {
						myObj = new File("/home/seth/uniwork/distributedSystem/eclipseWorkspace/assignment2/src/assignment2/data.txt");		    		
					}
					Scanner myReader = new Scanner(myObj);
					out.println("PUT"); // Send PUT request 
					
					while (myReader.hasNextLine()) {
						String dataRaw = myReader.nextLine();
						data = dataRaw.split(":",2);	
						
						if(data[1] != null) {
							jsonFile.put(data[0], data[1]); // Might have a warning here but it's a library thing so I can't fix it
						}
						
						out.println(jsonFile);
						out.flush();
					}
					
					if(debug) {
						System.out.println(jsonFile);
					}
					socket.close();
					myReader.close();
				}
				System.out.println("Terminating program");
				return;
				
			} catch (IOException e) {
				System.out.println("An error occurred. Retrying connection in 2 second");
				e.printStackTrace();
				attempts--;
				Thread.sleep(5000);
			}
		}
		
		System.out.println("Retry attempts exceeded 5 times. Terminating");
		
	}
	
	public boolean promptCheck() {
		Scanner scn = new Scanner(System.in);  // Create a Scanner object
	    System.out.print(">");
	    String cmd = scn.nextLine();
	    System.out.println(cmd);
		if(cmd.equals("send")) {	// == comparison does not work, it will compare references instead of contents
			System.out.println("Sending");
			return true;
		}
		return false;
	}

}
