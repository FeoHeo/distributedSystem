//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ConnectException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//public static PrintWriter connect_node(ServerSocket socketInput , PrintWriter outStream) throws UnknownHostException, IOException {		// Connect to a node and return the output stream
//    	boolean connected = false;
//    	Socket outSocket = null;
//        while (!connected) {
//            try {
//                outSocket = new Socket("localhost", otherPort);
//                connected = true;
//            } catch (ConnectException e) {
//                log("Waiting for other acceptor to start...");
//            }
//        }
//        Socket inSocket = socketInput.accept();
//        PrintWriter acptOut = new PrintWriter(outSocket.getOutputStream() , true);
//        BufferedReader acptIn = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
//        
//        Thread responseThread = new Thread (() -> {
//        	try {
//        		// Handling other Acceptors
//        		String inputAcpt;
//        		acptOut.println("Ping from " + port + " node num " + (port%10 + 1));
//        		outStream.println("Ping 2");
//        		while((inputAcpt = acptIn.readLine()) != null) {
//    				if(inputAcpt.startsWith("Confirmed")) {	
//    					String[] logArr = inputAcpt.split(":");		// logArr[1] contain round number, [2] contain val and [0] is "Confirmed"
//    					Double roundInfo = Double.parseDouble(logArr[1]) + (otherPort % 10);
//    					Integer valInfo = Integer.parseInt(logArr[2]);
//    					list.put(roundInfo, valInfo);
//    					acptOut.println("Confirmed:"+logArr[1]+":"+logArr[2]);
//    					log("Stored round "+roundInfo+" with value "+valInfo);
//    				} else {
//    					log(inputAcpt);
//    				}
//        			
//        		}
//        	} catch (IOException e) {
//        		// TODO Auto-generated catch block
//        		e.printStackTrace();
//        	} finally {
//        		log("Connection finished");
//        	}
//        });
//        
//        responseThread.start();
//        return acptOut;
//    }



