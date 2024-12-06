package paxos;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.TreeMap;

public class SimpleAcceptor {
	static boolean isPaused = false;	// For pausing the node and not sending any message
    private static int port = 5000;       // Default port
    private static int biggestRoundId = 0; // default round Id
    private static int nodeNum = 9;    // default numbers of node, will change to 9 later
    private static int acceptVal = 0;
    private static int startPort = 5000;	// To connect to the other Acceptor
    private static int delay = 10;		// To simulate delay between messages
    private static Map<Double,Integer> list = new TreeMap<> ();
    
    
    private static void log(String message) {
        System.out.println(String.format("[Acceptor-%d] %s", port, message));
    }
    
    public static boolean handle_prepare(int roundId) {
        if(roundId > biggestRoundId) {
            log("Updating biggest round from " + biggestRoundId + " to " + roundId);
            biggestRoundId = roundId;
            return true;
        } else {
            return false;    
        }
    }
    
    public static void send_promise(PrintWriter outStream, int roundId) {
        log("Sending Promise for round " + roundId);
        outStream.println("Promise:" + roundId);
    }
    
    public static void send_accept(PrintWriter outStream, int acceptId , PrintWriter[] otherOut) {
        log("Sending Accept confirmation for value " + acceptId);
        outStream.println("Confirmed:" + acceptId + ":" + biggestRoundId);
        for(int i=0 ; i<nodeNum-1 ; i++) {
        	otherOut[i].println("Confirmed:" + acceptId + ":" + biggestRoundId);
        }
//        otherOut.println("Confirmed:" + acceptId + ":" + biggestRoundId);
    }
    
    
    // Connect to a node and return the output stream
    public static PrintWriter connect_node(ServerSocket socketInput , int index) throws UnknownHostException, IOException {  // socketInput is the current node's socket, index is to reach the other nodes
	boolean connected = false;
	Socket outSocket = null;
	int nodePort = startPort + index;
    while (!connected) {
        try {
            outSocket = new Socket("localhost", nodePort);
            connected = true;
        } catch (ConnectException e) {
            log("Waiting for other acceptor to start...");
        }
    }
    Socket inSocket = socketInput.accept();
    PrintWriter acptOut = new PrintWriter(outSocket.getOutputStream() , true);
    BufferedReader acptIn = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
    
    Thread responseThread = new Thread (() -> {
    	try {
    		// Handling other Acceptors
    		String inputAcpt;
    		acptOut.println("Ping from " + port + " node num " + (port%10 + 1));
    		while((inputAcpt = acptIn.readLine()) != null) {
				if(inputAcpt.startsWith("Confirmed")) {	
					String[] logArr = inputAcpt.split(":");		// logArr[1] contain proposed val, [2] contain round number and [0] is "Confirmed"
					if(Integer.parseInt(logArr[2]) > biggestRoundId) {	// Update round id from other nodes
						log("Update round id node from "+biggestRoundId+ " to " + logArr[2]);
						biggestRoundId = Integer.parseInt(logArr[2]);
					}
					Double roundInfo = Double.parseDouble(logArr[1]) + (nodePort%10) / 10.0; // Divide by Double makes it output a Double
					Integer valInfo = Integer.parseInt(logArr[2]);
					list.put(roundInfo, valInfo);
					log("Stored round "+roundInfo+" with value "+valInfo);
				} else {
					log(inputAcpt);
				}
    			
				inputAcpt = null;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		log("Connection finished");
    	}
    });
    
    responseThread.start();
    return acptOut;
}
    
    public static void main(String[] args) {	// For connection with Proposer
        SimpleAcceptor obj = new SimpleAcceptor();
        
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
            log("Using port " + port);
            if(args.length > 1) {
                startPort = Integer.parseInt(args[1]);
                log("Using Paxos start location " + startPort);
            } else {
            	log("Using default Paxos start location " + startPort);
            }
        } else {
            log("Using default port " + port);
        }
        
        if(args.length > 2) {
        	delay = Integer.parseInt(args[2]);
        	log("Using custom delay " + delay);
        } else {
        	log("Using default node delay " + delay);
        }
        
        if(args.length > 3) {
        	nodeNum = Integer.parseInt(args[3]); 
        	log("Using custom number of node " + nodeNum);
        } else {
        	log("Using default number of node" + nodeNum);
        }
        
            
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log("Server is listening on port " + port + " with starting round id: " + biggestRoundId);
            
            try (	
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            	
            ) {
                log("New client connected from " + clientSocket.getRemoteSocketAddress());
                
                
                
                // Stuff for sending out confirm msg
                PrintWriter[] acptOut = new PrintWriter[nodeNum-1];
                int index = 0;
                for(int i=0 ; i<nodeNum ; i++) {	// Start a thread for each other nodes
                	if(serverSocket.getLocalPort() != startPort+i) {
                		acptOut[index] = connect_node(serverSocket , i);
                		index++;
                	}
                	
                }
                
                int storedDelay = 0;
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log("Received: " + inputLine);
                    
                    try {		// To simulate delay between messages
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                    
                    // Handling Proposer
                    String[] arr = inputLine.split(":");
                    if (inputLine.startsWith("Prepare") && isPaused == false) {	// Process prepare request, reject if round is too small
                        int receivedRoundId = Integer.parseInt(arr[1]);
                        log("Received Prepare with round ID: " + receivedRoundId);
                        
                        if(handle_prepare(receivedRoundId)) {
                            log("Sending promise for round " + receivedRoundId);
                            send_promise(out, receivedRoundId);
                        } else {
                            log("Rejecting round " + receivedRoundId + " (current biggest round: " + biggestRoundId + ")");
                            out.println("Reject");
                        }
                        
                    } else if(inputLine.startsWith("Accept") && isPaused == false) {
                        acceptVal = Integer.parseInt(arr[1]);
                        log("Received Accept with value: " + acceptVal);
                        send_accept(out, acceptVal , acptOut);
                    } else if(inputLine.startsWith("Pause") && arr[1].equals(String.valueOf(port))) {
                    	log("Pausing currnet node " + port);
                    	isPaused = true;
                    	out.println("Paused");
                    } else if(inputLine.startsWith("Unpause") && arr[1].equals(String.valueOf(port))) {
                    	log("Unpausing currnet node " + port);
                    	isPaused = false;
                    	out.println("Unpaused");
                    } else if(inputLine.startsWith("NoDelay")) {
                    	if(delay == 0) {
                    		out.println("Delay is already 0");
                    	} else {
                    		storedDelay = delay;
                    		delay = 0;
                    	}
                    	out.println("Delay set to " + delay);
                    } else if(inputLine.startsWith("Delay")) {
                    	if(delay != 0) {
                    		out.println("There is already " + delay);
                    	} else {
                    		delay = storedDelay;
                    	}
                    } else if(isPaused == true) {
                    	out.println("Paused");
                    } else if(isPaused == false) {
                        // Echo other messages
                        out.println("Server echoes: " + inputLine);
                    }
                    
                }
                
                
            } catch (IOException e) {
                log("Exception handling client: " + e.getMessage());
            }
        } catch (IOException e) {
            log("Server exception: " + e.getMessage());
        }
    }
}