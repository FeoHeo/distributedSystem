package byzant;

import java.io.*;
import java.net.*;

public class SimpleProposer {
    static int biggestRoundId = 2;    // Default round Id
    static int proposedId = 5;        // Default proposed Id
    static int port = 5000;           // Default port
    static int nodeNum = 3;           // The number of other nodes to connect to.
    static int majorityCurr = 0;		// Number of nodes that agreed
    static int majorityReq = 5;		// Number of node required to be considered majority

    static class ConnectionHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private int nodeIndex;
        private int localPort;

        public ConnectionHandler(String hostName, int port, int nodeIndex) throws IOException {
        	this.localPort = port;
            this.socket = new Socket(hostName, localPort);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nodeIndex = nodeIndex;
        }

        @Override
        public void run() {
            System.out.println("Connected to server via port " + socket.getPort());
			
			// Start a separate thread for reading responses
			Thread responseThread = new Thread(() -> {
			    try {
			        String serverResponse;
			        while ((serverResponse = in.readLine()) != null) {
			            System.out.println("Server " + localPort + " response: " + serverResponse);
			            if (serverResponse.equals("Reject")) {
//			                synchronized (SimpleProposer.class) {
			                    biggestRoundId = biggestRoundId + 3;
//			                }
			                System.out.println("Retrying with higher round: " + biggestRoundId);
			                out.println("Prepare:" + biggestRoundId);
			            } else if (serverResponse.startsWith("Promise")) {
			            	majorityCurr++;	// Increment to reach majority
			            	System.out.println("["+this.localPort+"] responded majority is "+majorityCurr);
//			                out.println("Accept:" + proposedId);
			            }
			            
			            if(serverResponse.equals("B-Reject")) {
			            	biggestRoundId = biggestRoundId + 3;
			            	System.out.println("Retrying with higher b-round: " + biggestRoundId);
			            	out.println("B-Prepare:"+biggestRoundId);
			            } else if(serverResponse.startsWith("B-Promise")) {
			            	majorityCurr++;
			            	System.out.println("["+this.localPort+"] responded majority is "+majorityCurr);
			            }
			        }
			    } catch (IOException e) {
			        System.err.println("Error reading from server " + nodeIndex + ": " + e.getMessage());
			    }
			});
			responseThread.start();


        }
        

        private void cleanup() {	// Clean up connections. This is just util stuff
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }

        public void sendMessage(String message) {	// For sending message using the current thread
            if (out != null) {
            	// Check message to send message format
                if(message.equals("send")) {
                	out.println("Prepare:" + biggestRoundId);
                } else if(message.equals("ctest")) {	// For testing confirm and fault tolerance
                	out.println("Confirmed:5:2"); // Sample confirm message
                } else {
                	out.println(message); // Otherwise just send the normal message
                }
            } else {
            	System.err.println("No output stream established");
            }
        }
    }
    
    
    public static void sendAll(String msgInput , ConnectionHandler[] handlerInput) {	// Simple function that send msg to all node
    	for(int i=0 ; i<nodeNum ; i++) {
    		handlerInput[i].sendMessage(msgInput);
    	}
    }
    

    public static void main(String[] args) {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);	// Parse starting port
        } else {
        	System.out.println("Using default starting port " + port);
        }
        if (args.length > 1) {
            nodeNum = Integer.parseInt(args[1]);	// Parse number of nodes
        } else {
        	System.out.println("Using default number of nodes " + nodeNum);
        }
        majorityReq = (nodeNum / 2) + 1;	// Update definition of majority
        String hostName = "localhost";
        ConnectionHandler[] handlers = new ConnectionHandler[nodeNum];

        try {
            // Create and start a thread for each connection
            for (int i = 0; i < nodeNum; i++) {
            	System.out.println("Establishing on: " + (port + i));
                handlers[i] = new ConnectionHandler(hostName, port + i, i);
                handlers[i].start();
            }
            
            Thread monitorThread = new Thread(() -> {	// A thread that will monitor and send Accept when majority reached
                while (true) {
                	if(majorityCurr >= majorityReq) {
                		sendAll("Accept:"+proposedId,handlers);
                		majorityCurr = 0;
                	}
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            monitorThread.start();

         // Handle user input for all connections
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
            	if(userInput.equals("send")) {
            		majorityCurr = 0;	// Restart for new round
            	} else if(userInput.startsWith("send")) {
            		String[] inputArr = userInput.split(" ");
            		proposedId = Integer.parseInt(inputArr[1]);
            		sendAll(inputArr[0],handlers);
            		continue;
            	}
            	sendAll(userInput , handlers);
            	
            }
            
            // Wait for all threads to complete
            for (ConnectionHandler handler : handlers) {
                if (handler != null) {
                    try {
                        handler.join();
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted: " + e.getMessage());
                    }
                }
            }
            
            

        } catch (IOException e) {
            System.err.println("Could not establish connections: " + e.getMessage());
            System.exit(1);
        }
    }
}
